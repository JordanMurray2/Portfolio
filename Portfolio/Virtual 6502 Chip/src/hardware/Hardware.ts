/**
 * Super class for all hardware to manage common aspects such as logging requirements
 */

export class Hardware {

    constructor(id: number, name: String, debug: boolean) {
        this.id = id;
        this.name = name;
        this.debug = debug;
    }

    public id: number = 0;
    public name: String = "";
    public debug: boolean;

    /**
     * Method Name: logging
     *
     * Description: general method for debugging hardware
     *
     * Params: String message
     *
     * Return: none
     **/
    protected log(message: String): void {
        //get the current date to display for log output
        let date: Date = new Date();
        let millis: number = date.getTime();

        // There should be a "switch" (a local instance variable) that allows you to turn on and off debugging output
        //this.debug = false;
        if(this.debug == true){
          console.log(`[HW - ${this.name} id: ${this.id} - ${millis}]:  ${message}`);
      }
    }

}
