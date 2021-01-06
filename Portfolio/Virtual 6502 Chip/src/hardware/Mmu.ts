import {Hardware} from "./Hardware";
import {Memory} from "./Memory";
//import {Cpu} from "./Cpu";

export class Mmu extends Hardware{

    constructor(memory: Memory) {
        super(0, "MMU", true);
        this.memory = memory;
        //this.cpu = cpu;
        this.lowByte;
        this.highByte;
    }

    //connect the memory and CPU to the MMU so they can communicate, this is the Middleman between the two
    public memory: Memory;
    //private cpu: Cpu;
    public lowByte: number;
    public highByte: number;

    /*
     *  methods to store high and low order bytes
     */
    public setLowByte(lowByte: number): void {this.lowByte = lowByte;}
    public setHighByte(highByte: number): void {this.highByte = highByte}

    /*
     * method used to set the MAR when given a 16-bit address
     *
     * param: address
     */
    public setMAR(address: number): void{
        this.memory.setMAR(address);
    }

    /*
     * method for converting little-endian format to a normal 16-bit address
     *
     * params: highByte, lowByte
     */
    public setMAR_little_endian(): void{
        //set the helper var to the base of the numbers passed in (16)
        let helper: number = 0x10;

        //until the helper variable is larger than the lowest byte, multiple the helper variable by the base (16)
        while(this.lowByte >= helper){
            helper *= 0x10;
        }

        //this is the statement that concatinates the two bytes together in the correct order
        let marAdd: number = this.highByte * helper + this.lowByte;
        //set the MAR
        this.setMAR(marAdd);
        this.log(`MAR address: ${marAdd.toString(16)}`);  //log statement that prints out the address calculated
    }

    /*
     * this method will read memory at the location of MAR and then update the MDR
     */
    public read(): void {
        let currData: number = this.memory.getMemory(this.memory.getMAR());
            this.memory.setMDR(currData);
    }

    /*
     * this method will write the contents of the MDR to the address at the MAR
     */
    public write(): void {
        this.memory.setMemory(this.memory.getMAR(), this.memory.getMDR());
    }
    /*
     * directly writes to memory without calling MAR or MDR (flash memory)
     *
     * params: address, data
     */
    public writeImmediate(address: number, data: number){
        this.memory.setMemory(address, data);
    }

    /*
     * print a chunk of values from memory starting at one address and ending at another
     *
     * params: fromAddress, toAddress
     */
    public memoryDump(fromAddress: number, toAddress: number): void{
        this.log("Memory Dump: Debug");
        this.log("---------------------------------");
        //loop through all of the addresses starting at the from address up to and including the to address
        for(let i = fromAddress; i<= toAddress; i++){
            //store the data from the current address in a var
            let currData = this.memory.getMemory(i).toString(16);
            //if the current data is only one character, you have to pad it with a zero to make it look pretty :)
            if(currData.length == 1){
                currData = "0" + currData;
            }
            //print the data at the current address
            this.log(`Addr: ${i.toString(16).toUpperCase()} | ${currData.toUpperCase()}`);
        }
        this.log("---------------------------------");
        this.log("Memory Dump: Complete");
    }
}
