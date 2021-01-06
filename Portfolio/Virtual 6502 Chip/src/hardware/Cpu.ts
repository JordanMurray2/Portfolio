/* ------------
     CPU.ts

     Routines for the host CPU simulation, NOT for the OS itself.
     In this manner, it's A LITTLE BIT like a hypervisor,
     in that the Document environment inside a browser is the "bare metal" (so to speak) for which we write code
     that hosts our client OS. But that analogy only goes so far, and the lines are blurred, because we are using
     TypeScript/JavaScript in both the host and client environments.

     This code references page numbers in the text book:
     Operating System Concepts 8th edition by Silberschatz, Galvin, and Gagne.  ISBN 978-0-470-12872-5

     BG: I am augmenting the original CPU to be function inclusive and to have a basic fetch - decode - execute
        3-stage pipeline.  This should be fun.

     Plan for Decoding:
        Based on the Cpu.Mode enum setting, the CPU will either live in a fantasy world where it is not used (kernel,
        fudge) Or actually used just like a real processor (user).

        This is the initial plan because otherwise I would need to write a compiler, or write the entire OS kernel and
        supporting user space applications like the shell in 6502 assembler which is cool but I only have a week before
        the semester starts.

        So this trade off means that the 'CPU' will not actually be running when the Kernel is in charge or a
        application given 'fudge' mode privilege.  When the CPU should be in kernel mode, it will *not* be pulling
        opcodes from memory and decoding, the kernel TypeScript code will just be running directly. Anytime it is
        supposed to be in user mode but running an application I do not want to write in 6502 assembler it will be in
        a mix of user and kernel mode. Kernel mode because it will not actually be getting opcodes from memory and
        decoding them, and user mode because it will lack privileges of kernel mode.  Hence the name, fudge mode (this
        is a teaching tool after all).  But when switched into user mode it will pull opcodes from memory and decode
        them just like a real CPU.  This means that all applications in user space need to written or eventually
        compiled to compatible 6502 opcodes in the correct endian format. TODO:  More to come on this soon.
     ------------ */


import {Hardware} from "./Hardware";
import {ClockListener} from "./imp/ClockListener";
import {Interrupt} from "./imp/Interrupt";
import {QueueMurray} from "./QueueMurray";
import {Mmu} from "./Mmu";
import {Ascii} from "./Ascii";

export class Cpu extends Hardware implements ClockListener{

    constructor(mmu: Mmu) {
        super(0, "CPU", true);
        this.pc = 0x0000;
        this.acc = 0x00;
        this.xReg = 0x00;
        this.yReg = 0x00;
        this.ir = 0x00;
        this.zFlag = 0x00;
        this.interrupt = null;
        this.mode = Cpu.Mode.KERNEL;
        this.pipelineStep = Cpu.PipelineSteps.fetch;
        this.isExecuting = false;
        this.clockCount = 0;
        this.mmu = mmu;
        this.writeBackData = 0x00;
        //log the creation of this hardware
        this.log("CPU created");
    }

    public pc: number;
    public acc: number;
    public xReg: number;
    public yReg: number;
    public ir: number;
    public zFlag: number;
    public interrupt: Interrupt;
    public mode: Cpu.Mode;
    public pipelineStep: Cpu.PipelineSteps;
    public isExecuting: boolean;
    public clockCount: number;
    public mmu: Mmu;
    public writeBackData : number;

    public reset(): void {
        this.pc = 0x0000;
        this.acc = 0x00;
        this.xReg = 0x00;
        this.yReg = 0x00;
        this.ir = 0x00;
        this.zFlag = 0;
        this.interrupt = null;
        this.mode = Cpu.Mode.KERNEL;
        this.pipelineStep = Cpu.PipelineSteps.fetch;
        this.isExecuting = false;
        this.clockCount = 0;
        this.writeBackData = 0x00;
    }

    /**
     * Send the CPU an interrupt here!
     * @param interrupt
     */
    public setInterrupt(interrupt: Interrupt): void {
        this.interrupt = interrupt;
    }

    /**
     * CPU acts on the clock pulse, implementation goes here.
     */
    public pulse(): void {
        //log the response to the clock pulse
        this.log(`recieved clock pulse - Clock Count: ${this.clockCount} - Mode: ${this.mode}`);
        this.cpuStateLog(); //keep in mind this is printing the curr step before actually performing the action
        //increment the clock count at the end of the clock cycle
        this.clockCount++;
        //switch to handle the current step of the pipeline and call the correct method
        switch(this.pipelineStep) {
            case 1: {
                this.fetch();
                break;
            }
            case 2: {
                this.decode1();
                break;
            }
            case 3:{
                this.decode2();
                break;
            }
            case 4:{
                this.execute1();
                break;
            }
            case 5:{
                this.execute2();
                break;
            }
            case 6:{
                this.writeBack();
                break;
            }
            case 7: {
                //check for an interrupt at the end of a clock cycle
                let currInterrupt = this.chkInterrupt();
                //if there is an interrupt, print the contents of the output buffer
                if(currInterrupt == true){
                    //this.pipelineStep = Cpu.PipelineSteps.chkInterrupt;
                    //call a helper method that makes it easier to print the queue
                    let buffer: String[] = this.interrupt.outputBuffer.helpPrintQueue();
                    if(buffer.length != 0){
                        let newBuffer = buffer.join("] [");
                        this.log(`CPU sees the Buffer Contains: [${newBuffer}] `);
                        //remove the current interrupt from the output buffer
                        this.interrupt.outputBuffer.dequeue();
                    }
                }
                //reset the step back to 1 to go through instruction cycle again
                this.pipelineStep = Cpu.PipelineSteps.fetch;
                break;
            }
        }
    }

    /**
     * Handles the fetch portion of the cycle
     * Checks PC, retrieves instruction form Memory
     */
    private fetch(): void {
        //set the MAR to the Program counter to fetch the op code
        this.mmu.setMAR(this.pc);
        //read will set the MDR value
        this.mmu.read();
        //set ir with the data fetched from memory
        this.ir = this.mmu.memory.getMDR();
        //increment the pc and the current pipeline step
        this.pc++;
        this.pipelineStep++;
    }

    /**
     * Handles decode of instruction based on 6502 instruction set
     */
    private decode1(): void {
        //figure out the number of operands
        let numOperands = this.searchEnums(this.ir);
        //put in a switch that depending on the number of operands, what actions should take place
        switch(numOperands){
            //op codes with no operands
            case 0: {
                if(this.ir == 0xFF){ //Make a system call
                    if(this.xReg == 0x01){ //print the value in the yReg
                        this.pipelineStep += 2; //send pipeline to execute 1
                    } else if(this.xReg == 0x02){ //print a value in a memory location
                        //get the low order byte
                        this.mmu.setMAR(this.pc);
                        this.mmu.read();
                        this.mmu.lowByte = this.mmu.memory.getMDR();
                        //increment pc and send to decode2 to get the high order byte
                        this.pc++;
                        this.pipelineStep += 1;
                    }
                }else{ //if this.ir == 0xEA (No operation, so just check for interrupt)
                    this.pipelineStep += 5;
                }
                break;
            }
            //opcodes with 1 operand
            case 1: {
                if(this.ir == 0xA9){ //load the acc with a constant
                    this.pipelineStep += 2;
                } else if (this.ir == 0xA2) { //load the xReg with a constant
                    this.pipelineStep += 2;
                } else if (this.ir == 0xA0){ //load the yReg with a constant
                    this.pipelineStep += 2;
                } else { //If this.ir == 0xD0 (branching)
                    if(this.zFlag == 1){ //branch should happen, so go to execute1
                        //this.pc++;
                        this.pipelineStep += 2;
                    } else { //if they are equal (zFlag == 0), continue on and don't branch
                        this.pc += 1;
                        this.pipelineStep += 5;
                    }
                }
                break;
            }
            //opcodes with 2 operands
            case 2:{
                if(this.ir == 0xAD){ //Load the acc from memory
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else if (this.ir == 0x8D ){ //store the acc in memory
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else if (this.ir == 0x6D ){ //add with carry
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else if (this.ir == 0xAE ){ //load the xReg from memory
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else if (this.ir == 0xAC ){ //Load the yReg from memory
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else if (this.ir == 0xEC ){ //Compare a byte in memory to xReg to set zFlag
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                } else { //if this.ir == 0xEE (increment the value of a byte)
                    //get the low order byte
                    this.mmu.setMAR(this.pc);
                    this.mmu.read();
                    this.mmu.lowByte = this.mmu.memory.getMDR();
                    this.pc++;
                    this.pipelineStep += 1;
                }
                break;
            }
        }

    }

    /**
     * Handles decode for all values with 2 operands
     */
    private decode2(): void {
        if(this.ir == 0xAD){ //Load the acc from memory
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0x8D ){ //Store the acc in memory
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0x6D ){ //Add with carry
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0xAE ){ //Load the xReg from memory
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0xAC ){ //Load the yReg from memory
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0xEC ){ //compare a byte in memory to xReg and set Zflag
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        } else if (this.ir == 0xFF){ //Make a system call (xReg == 2, so print val from memory)
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        }else { //if this.ir == 0xEE (Increment the value of a byte)
            //fetch the high order byte
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            this.mmu.highByte = this.mmu.memory.getMDR();
            this.pc++;
            this.pipelineStep += 1;
        }
    }

    /**
     * Executes the current instruction in the pipeline
     */
    private execute1(): void {
        if(this.ir == 0xA9){ //load the acc with a constant
            //fetch the constant from the next memory address
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            //put the data in the acc
            this.acc = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
            this.pc++;
        } else if (this.ir == 0xA2){ //load the xReg with a constant
            //fetch the constant
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            //put the data in the xReg
            this.xReg = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
            this.pc++;
        } else if (this.ir == 0xA0){ //load the yReg with a constant
            //fetch the constant
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            //put the data in the yReg
            this.yReg = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
            this.pc++;
        } else if (this.ir == 0xAD) { //load the acc from memory
            //concatinate the address into regular format and fetch data
            this.mmu.setMAR_little_endian();
            this.mmu.read();
            //put the data in the acc
            this.acc = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
            this.pc++;
        } else if (this.ir == 0xAE){ //load the xReg from memory
            //concatinate the address into regular format and fetch the data
            this.mmu.setMAR_little_endian();
            this.mmu.read();
            //put the data in the xReg
            this.xReg = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
            this.pc++;
        } else if (this.ir == 0xAC){ //load the yReg from memory
            //concatinate the address into  regular format and fetch data
            this.mmu.setMAR_little_endian();
            this.mmu.read();
            //put the data in the yReg
            this.yReg = this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
        } else if (this.ir == 0x8D){ //store the acc in memory
            //do the little endian conversion
            this.mmu.setMAR_little_endian();
            //store the acc data in the MDR
            this.mmu.memory.setMDR(this.acc);
            //call the write function to store the data in memory
            this.mmu.write();
            //check for interrupt
            this.pipelineStep += 3;
        } else if (this.ir == 0x6D){ //Add with carry
            //do the litte endian concatination
            this.mmu.setMAR_little_endian();
            //read the value and add it to the acc
            this.mmu.read();
            this.acc += this.mmu.memory.getMDR();
            //check for interrupt
            this.pipelineStep += 3;
        } else if(this.ir == 0xEC){ //compare byte in memory to xReg and set zFlag if ==
            //do the little endian concatination
            this.mmu.setMAR_little_endian();
            //read the data at that location
            this.mmu.read();
            //if they are equal, zflag is zero, which means you would not branch, and continue with program
            if(this.mmu.memory.getMDR() == this.xReg){
                this.zFlag = 0;
            } else {
                this.zFlag = 1; //if z flag is 1, program branches to the spot in the program you want
            }
            //check for interrupt
            this.pipelineStep += 3;
        } else if(this.ir == 0xD0){ //branch
            //set the MAR to the pc, to get the correct number to branch
            this.mmu.setMAR(this.pc);
            this.mmu.read();
            //store the data from memory in a variable
            let branchNum = this.mmu.memory.getMDR().toString(2);
            //Debug Statement:
            //this.log(`Number to branch : ${branchNum}`);
            if(branchNum.length < 8){ //if the first char of string is 0
                this.pc += parseInt(branchNum, 2); //just add it to the pc
            } else {
                while(branchNum.length < 16){ //pad the number with 1s so length is equal to the program counter
                    branchNum = "1"+branchNum;
                }
                //Debug Statement:
                //this.log(`Padded branch number is : ${branchNum}`);
                let totalBranch = this.pc + parseInt(branchNum, 2);
                //Debug Statement:
                //this.log(`total branch number is : ${totalBranch}`);
                let addQueue = new QueueMurray;
                //split the string up by character (dropping the first bit will have it loop back around and go back the correct # of memory addresses)
                for(let i = 0; i < totalBranch.toString(2).length; i++){
                    //store the string value by each char in a queue
                    addQueue.enqueue(totalBranch.toString(2).charAt(i));
                }
                addQueue.dequeue(); //drop the first number because it will carry over
                let stringBranchNum = null; //var to put string back together with carry bit dropped
                //put the string back together one char at a time
                while(!addQueue.isEmpty()){
                    if(stringBranchNum != null){
                        stringBranchNum = stringBranchNum + addQueue.dequeue();
                    }else{
                        stringBranchNum = addQueue.dequeue();
                    }
                }
                //Debut Statement:
                //this.log(`Number after dropping the overflow is : ${parseInt(stringBranchNum.toString(),2)}`);
                //convert the string back to a number
                let finalBranch = parseInt(stringBranchNum.toString(),2);
                //set the pc to the branch number you want to go back to
                this.pc = finalBranch;
            }
            //check for interrupt
            this.pipelineStep += 3;
        } else if(this.ir == 0xFF){ //system call
            if(this.xReg == 0x01){ //if the xReg has a 1 in it, print the val of yReg
                //format the yReg value
                let yRegPrint = this.yReg.toString(16).toUpperCase();
                //pad value if needed
                if(yRegPrint.length == 1){
                    yRegPrint = "0" + yRegPrint;
                }
                //Show the user
                this.log(`SYSTEM CALL - The value in the yReg = ${yRegPrint}`);
            } else if(this.xReg == 0x02){ //if the xReg has a 2 in it, print a value from memory
                //do the little endian concatination
                this.mmu.setMAR_little_endian();
                this.mmu.read();
                let currData = this.mmu.memory.getMDR();
                this.log("Ascii Table Output: ");
                //print the values in the memory addresses and loop until there is none left (00)
                while(currData != 0x00){
                    let ascii = new Ascii(currData);
                    let asciiVal = ascii.searchTable();
                    process.stdout.write(`${asciiVal}`);
                    this.mmu.setMAR(this.mmu.memory.getMAR()+1);
                    this.mmu.read();
                    currData = this.mmu.memory.getMDR();
                }
                //just to make output look pretty
                console.log("\n");
            }
            //go to check interrupt
            this.pipelineStep += 3;
        } else if(this.ir == 0xEE){ //increment the value of a byte
            //do the little endian concatination and the read which sets the MDR
            this.mmu.setMAR_little_endian();
            this.mmu.read();
            //finish this in execute2
            this.pipelineStep++;
        }
    }

    /**
     * Executes the current instruction in the pipeline
     * the only method that finishes here is 0xEE(incrementing the value of a byte)
     */
    private execute2(): void {
        //add 1 to the MDR and store it in a var to be written back into memory
        this.writeBackData = this.mmu.memory.getMDR() + 1;
        //go to writeback
        this.pipelineStep++;
    }

    /**
     * writes data back from the MDR into a memory location
     */
    private writeBack(): void {
        //set the MDR with the data to be written back
        this.mmu.memory.setMDR(this.writeBackData);
        //writeback the data
        this.mmu.write();
        //check for interrupt
        this.pipelineStep++;
    }

    /**
     * Checks if an interrupt has been sent from the Interrupt Controller
     *
     * return: boolean
     */
    public chkInterrupt(): boolean {
        let ans: boolean = false;
        if(this.interrupt != null){
            //if there is an interrupt print out the priority and name
            this.log(`CPU acting on Interrupt - IRQ : ${this.interrupt.priority} from: ${this.interrupt.name}`);
            ans = true;
        }
        return ans;
    }
    /*
     * method to help print out the variables of the CPU
     */
    public cpuStateLog(): void {
        //format the variables
        let pcPrint = this.pc.toString(16).toUpperCase();
        let irPrint = this.ir.toString(16).toUpperCase();
        let accPrint = this.acc.toString(16).toUpperCase();
        let xRegPrint = this.xReg.toString(16).toUpperCase();
        let yRegPrint = this.yReg.toString(16).toUpperCase();

        //all of the registers that are hex values need to be padded with zeros for neater output
        while(pcPrint.length <  4){
            pcPrint = "0" + pcPrint;
        }
        //Pad values to make them look nicer
        if(irPrint.length == 1){
            irPrint = "0" + irPrint;
        }
        if(accPrint.length == 1){
            accPrint = "0" + accPrint;
        }
        if(xRegPrint.length == 1){
            xRegPrint = "0" + xRegPrint;
        }
        if(yRegPrint.length == 1){
            yRegPrint = "0" + yRegPrint;
        }
        //print out the log
        this.log(`CPU State | Mode: ${this.mode} PC: ${pcPrint} IR: ${irPrint} Acc: ${accPrint} xReg: ${xRegPrint} yReg: ${yRegPrint} zFlag: ${this.zFlag} Step: ${this.pipelineStep}`);
    }

    /**
     * Search the Enums to find OP code to figure out the number of operands
     *
     * Param: currOPCode
     *
     * returns: number
     */
    public searchEnums(currOPCode: number): number {
        //search enum with 0 opcodes
        for(const opcodes0 in Cpu.opCodes0){
            //if the OP code is found, return 0 so program knows there is 0 operands
            if(currOPCode.toString() == opcodes0){
                return 0;
            }
        }

        //search enum with 1 opcodes
        for(const opcodes1 in Cpu.opCodes1){
            //if the OP code is found, return 1 so program knows there is 1 operand
            if(currOPCode.toString() == opcodes1){
                 return 1;
            }
        }

        //search enum with 2 opcodes
        for(const opcodes2 in Cpu.opCodes2){
            //if the OP code is found, return 2 so program knows there is 2 operands
            if(currOPCode.toString() == opcodes2){
                return 2;
            }
        }
    }
 }

export namespace Cpu {
    /*
        KERNEL = CPU not decoding opcodes, emulating protected mode
        USER = CPU running decoding opcodes and running applications in userspace
        FUDGE = CPU not decoding opcodes, not emulating protected mode
     */
    export enum Mode {
        KERNEL, USER, FUDGE
    }

    //different steps that correspond with methods, to keep track of the current step in program (instruction cycle)
    export enum PipelineSteps{
        fetch = 1,
        decode1 = 2,
        decode2 = 3,
        execute1 = 4,
        execute2 = 5,
        writeBack = 6,
        chkInterrupt = 7,
    }

    //enum that stores all opcodes with 0 operands
    export enum opCodes0{
        nop = 0xEA,
        brk = 0x00,
        sys = 0xFF,
    }

    //enum that stores all opcodes with 1 operand
    export enum opCodes1{
        ldaC = 0xA9,
        ldxC = 0xA2,
        ldyC = 0xA0,
        bne = 0xD0,
    }

    //enum that stores all opcodes with 2 operands
    export enum opCodes2{
        ldaM = 0xAD,
        sta = 0x8D,
        adc = 0x6D,
        ldxM = 0xAE,
        ldyM = 0xAC,
        cpx = 0xEC,
        inc = 0xEE,
    }
}
