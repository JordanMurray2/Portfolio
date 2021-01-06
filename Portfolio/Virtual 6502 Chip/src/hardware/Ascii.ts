export class Ascii {
    //class takes in the ascii val from cpu to convert from hex to ascii
    constructor(target: number){
        this.target = target;
    }

    public target : number;

    //Search the ascii table to see if the value exists
    public searchTable():string{
        //get the value from the enumeration
        let returnVal = Ascii.AsciiTable[this.target];
        //default return value is space
        if(returnVal == undefined){
            return " ";
        }
        //return the appropriate value to the class to be printed out
        if(returnVal == "zero"){
            return "0";
        } else if (returnVal == "one"){
            return "1";
         } else if (returnVal == "two"){
            return "2";
        } else if (returnVal == "three"){
            return "3";
        } else if (returnVal == "four"){
            return "4";
        } else if (returnVal == "five"){
            return "5";
        } else if (returnVal == "six"){
            return "6";
        } else if (returnVal == "seven"){
            return "7";
        } else if (returnVal == "eight"){
            return "8";
        } else if (returnVal == "nine"){
            return "9";
        } else if (returnVal == "space"){
            return " ";
        } else if (returnVal == "period"){
            return ".";
        } else if (returnVal == "hyphen"){
            return "-";
        } else if (returnVal == "exclamation"){
            return "!";
        } else if (returnVal == "semiColon"){
            return ";";
        } else if (returnVal == "colon"){
            return ":";
        } else if (returnVal == "return"){
            return "\n";
        } else {
            return returnVal;
        }
    }
}

//emumeration to store the ascii conversions
export namespace Ascii{
    export enum AsciiTable{
        //captial alphabet
        A = 0x41,
        B = 0x42,
        C = 0x43,
        D = 0x44,
        E = 0x45,
        F = 0x46,
        G = 0x47,
        H = 0x48,
        I = 0x49,
        J = 0x4A,
        K = 0x4B,
        L = 0x4C,
        M = 0x4D,
        N = 0x4E,
        O = 0x4F,
        P = 0x50,
        Q = 0x51,
        R = 0x52,
        S = 0x53,
        T = 0x54,
        U = 0x55,
        V = 0x56,
        W = 0x57,
        X = 0x58,
        Y = 0x59,
        Z = 0x5A,
        //lowercase alphabet
        a = 0x61,
        b = 0x62,
        c = 0x63,
        d = 0x64,
        e = 0x65,
        f = 0x66,
        g = 0x67,
        h = 0x68,
        i = 0x69,
        j = 0x6A,
        k = 0x6B,
        l = 0x6C,
        m = 0x6D,
        n = 0x6E,
        o = 0x6F,
        p = 0x70,
        q = 0x71,
        r = 0x72,
        s = 0x73,
        t = 0x74,
        u = 0x75,
        v = 0x76,
        w = 0x77,
        x = 0x78,
        y = 0x79,
        z = 0x7A,
        //numbers
        zero = 0x30,
        one = 0x31,
        two = 0x32,
        three = 0x33,
        four = 0x34,
        five = 0x35,
        six = 0x36,
        seven = 0x37,
        eight = 0x38,
        nine = 0x39,
        //special characters
        space = 0x20,
        period = 0x2E,
        hyphen = 0x2D,
        exclamation = 0x21,
        semiColon = 0x3B,
        colon = 0x3A,
        enter = 0x0D,
    }
}