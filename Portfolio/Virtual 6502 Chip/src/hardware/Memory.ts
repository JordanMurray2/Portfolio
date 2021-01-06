/* ------------
     Memory.ts
        TODO: BG
     .../
     ------------ */

import {Hardware} from "./Hardware";
import {ClockListener} from "./imp/ClockListener";
import {Mmu} from "./Mmu";

export class Memory extends Hardware implements ClockListener{

    constructor() {
        super(0, "RAM", false);
        this.mar = 0x0000;
        this.mdr = 0x00;
        this.isExecuting = false;
        this.memory = new Array(65536);
        //log the creation of this hardware
        this.log(`Memory created - Addressable Space : ${this.memory.length}`);
    }

    public mar: number;
    public mdr: number;
    public isExecuting: boolean;
    public memory: number[];

    //declare getter methods to be called by MMU
    public getMAR(): number{return this.mar;}
    public getMDR(): number{return this.mdr;}
    public getMemory(address: number): number{return this.memory[address-1];}

    //declare setter methods to be called by MMU
    public setMAR(mar: number){this.mar = mar;}
    public setMDR(mdr: number){this.mdr = mdr;}
    public setMemory(address: number, data: number){this.memory[address-1] = data;}

    /*
     * reset the values for memory
     */
    public reset(): void {
        this.mar = 0x00;
        this.mdr = 0x00;
        this.isExecuting = false;
        this.initializeMemory();
    }

    /*
     * method that loops through memory array to set all of the values to 0
     */
    public initializeMemory(): void{
        for(let i = 0; i < 65536; i++){
            this.memory[i] = 0x00;
        }
    }

    /*
     * pulse method for the clock listener interface
     */
    public pulse(): void {
        //log the response to the clock pulse
        this.log("Recieved Clock Pulse");
    }
}
