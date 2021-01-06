
export class Driver {
    constructor(id: number, name: String) {
        this.id = id;
        this.name = name;

    }

    public id: number = 0;
    public name: String = "";

    // will have a function assigned (the driver)
    //public isr: function;


    public debug: boolean = true;

    protected log(message: String): void {
        if(this.debug) {
            // Note the REAL clock in milliseconds since January 1, 1970.
            var now: number = new Date().getTime();

        }
    }

}
