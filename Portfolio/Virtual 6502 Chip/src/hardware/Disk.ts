/* ------------
     Disk.ts
        TODO: BG
     ...
     ------------ */

export class Disk {

    constructor(public stuff: number = 0,
                public isExecuting: boolean = false) {

    }

    public init(): void {
        this.stuff = 0;
        this.isExecuting = false;
    }

    public cycle(): void {


    }
}
