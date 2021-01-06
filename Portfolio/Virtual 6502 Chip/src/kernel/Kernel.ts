/* ------------
     Kernel.ts
        TODO: BG
     ...
     ------------ */

module Kernel {

    export class Kernel {

        constructor(public stuff: number = 0,
                    public isExecuting: boolean = false) {
            //console.log("[hardware: disk] - disk Created");
        }

        public init(): void {
            this.stuff = 0;
            this.isExecuting = false;
        }


    }
}