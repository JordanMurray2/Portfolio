/**
 * file       : AESCipher.go
 * author     : Jordan Murray
 * course     : CMPT 630
 * assignment : Final Project
 * due date   : May 23, 2021
 * version    : 1.0
 */
package main

import "strconv"
import "strings"
import "encoding/hex"
import "crypto/rand"

// parseFile
// parse the file and call Aes method on every 16 byte string of file
// param: string -- hex string version of the byte[] of the file passed in
// return: string -- the hex string representation of the encrypted file
func parseFile(content string) string {
    //call random generator method, to get a hex value of size 16 bytes to use as encryption key
    key, _ := randomHex(16)

    //split string into 16 bytes at a time and feed through the AES algorithm
    var strCountStart = 0
    var strCountEnd = 32

    //value to store all the cipher text
    var output string = ""
    for(strCountEnd < len(content)){
        var inputStr = content[strCountStart:strCountEnd]
        output += Aes(inputStr, key)
        strCountEnd += 32
        strCountStart += 32
    }
    //this is to get the remainder of the bytes and pad with space char if not
    if(strCountStart < len(content)){
        var inputStr = content[strCountStart:len(content)]
        //need to pad with space Char until the length of the string is 32
        for(len(inputStr) < 32){
            inputStr = inputStr + "20"
        }
        output += Aes(inputStr, key)
    }
    return output
}

// randomHex
// method for generating a random hex number for the encryption key
// param: int the number of bytes to generate a key for
// return: string of hex that is the encryption key and an error if found
func randomHex(n int) (string, error) {
  //make a byte array equal to the size of the key needed
  bytes := make([]byte, n)
  //generate the hex
  if _, err := rand.Read(bytes); err != nil {
    return "", err
  }
  return hex.EncodeToString(bytes), nil
}

// Aes
// main method for the AES encryption process. Generates 11 round keys and loops until the encryption is processed
// param: pTextHex string with the hex to be encrypted, keyHex string with the starting key to use for encryption
// return: string with the output of the 16 byte encryption
func Aes(pTextHex string, keyHex string) string {
    var cTextHex string = ""
    var stateMat [4][44]string
    var keyMat [4][44]string
    var keyMat2 [4][44]string
    var stringCounter int = -2

    //send the key to the method to generate 11 rounds and store in array
    var aesRoundKeys []string = make([]string,11,11)
    aesRoundKeys = aesRoundKeyss(keyHex)

    var col, row int
    //set the plain text hex and key into column major matrices
    for col = 0; col < len(stateMat); col++ {
        for row = 0; row < len(stateMat); row++ {
            stringCounter+=2
            var createKHex string = string(keyHex[stringCounter]) + string(keyHex[stringCounter +1])
            var createPTHex string = string(pTextHex[stringCounter]) + string(pTextHex[stringCounter +1])
            stateMat[row][col] = createPTHex
            keyMat[row][col] = createKHex
        }
    }

    //start the process
    stateMat = AESStateXOR(stateMat, keyMat)

    var i int
    //loop through the round keys to generate output
    for i = 1; i < len(aesRoundKeys); i++ {
        //substitute the bytes in the state array
        stateMat = AESNibbleSub(stateMat)

        //shift the rows of the state Matrix
        stateMat = AESShiftRow(stateMat)

        // mix the columns if it is not the last round
        if(i != len(aesRoundKeys)-1){
            stateMat = AESMixColumn(stateMat)
        }

        stringCounter = -2;
        var col, row int
        //put the key into a matrix for xOR operation
        for col = 0; col < len(keyMat2); col++ {
            for row = 0; row < len(keyMat2); row++ {
                stringCounter+=2
                var createKHex = string(aesRoundKeys[i][stringCounter]) + string(aesRoundKeys[i][stringCounter +1])
                keyMat2[row][col] = createKHex
            }
        }

        stateMat = AESStateXOR(stateMat, keyMat2);

        //if its the last round, set the output
        if(i == len(aesRoundKeys)-1) {
            var col, row int

            for col = 0; col < len(stateMat); col++ {
                for row = 0; row < len(stateMat); row++ {
                    var output string = stateMat[row][col]
                    if (cTextHex == "") {
                        if (len(output) == 1) {
                            cTextHex = "0" + output
                        } else {
                            cTextHex = output
                        }
                    } else {
                        if (len(output) == 1) {
                            cTextHex += "0" + output
                        } else {
                            cTextHex += output
                        }
                    }
                }
            }
        }
    }
    return strings.ToUpper(cTextHex);
}

// aesRoundKeyss
// method that generates the 11 round keys
// param: keyHex string that is the first round key
// return: string [] that contains the 11 round keys
func aesRoundKeyss(keyHex string) []string {
        var roundKeysHex []string = make([]string, 11)
        var kMat [4][44]string
        var w [4][44]string
        var stringCounter int = -2
        var currRound int = 0
        var temp []string = make([]string, 4)
        var wCounter int = 3
        var helper int = 1

        //set the first round since it is the same as the key generated
        roundKeysHex[0] = keyHex

        var col, row int
        //set the values of the first round key input here and set the first 4 words in w
        for col = 0; col < len(kMat); col++ {
            for row = 0; row < len(kMat); row++ {
                stringCounter+=2
                var val1  = string(keyHex[stringCounter])
                var val2  = string(keyHex[stringCounter+1])
                var createHex string = val1 + val2
                kMat[row][col] = createHex
                w[row][col] = createHex
            }
        }
        var i int
        //loop the algorithm 10 times to get the rest of the keys
        for i = 1; i < 11; i++{
            //increment the round, since it started at 0
            currRound++

            //perform the circular shift on the bytes in the 4th column for the curr round
            temp[0] = w[1][wCounter]
            temp[1] = w[2][wCounter]
            temp[2] = w[3][wCounter]
            temp[3] = w[0][wCounter]

            //perform the byte substitution
            temp[0] = aesSBox(temp[0])
            temp[1] = aesSBox(temp[1])
            temp[2] = aesSBox(temp[2])
            temp[3] = aesSBox(temp[3])

            //XOR the values current round constant and the first value in the temp array
            if(len(temp[0]) == 1){
                 temp[0] = "0" + temp[0]
            }
            data, err := hex.DecodeString(temp[0])
            var rconVal string = ""
            //might need to fix this
            if(len(aesRcon(currRound)) == 1){
               rconVal = "0" + aesRcon(currRound)
            } else {
               rconVal = aesRcon(currRound)
            }
            data2, err := hex.DecodeString(rconVal)
            if err != nil {
                panic(err)
            }
            temp[0] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
            if(len(temp[0]) == 1){
                temp[0] = "0" + temp[0]
            }

            //These next 4 blocks of code  fill in the next 4 columns of w which will get concatenated to produce the key
            //block 1
            if(len(w[0][wCounter-3]) == 1){
               w[0][wCounter-3] = "0" + w[0][wCounter-3]
            }
            data, err = hex.DecodeString(w[0][wCounter-3])
            if(len(temp[0]) == 1){
                 temp[0] = "0" + temp[0]
            }
            data2, err = hex.DecodeString(temp[0])
            if err != nil {
                    panic(err)
            }
            w[0][wCounter+1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
            if(len(w[0][wCounter+1]) == 1){
                w[0][wCounter+1] = "0" + w[0][wCounter+1]
            }

            if(len(w[1][wCounter-3]) == 1){
               w[1][wCounter-3] = "0" + w[1][wCounter-3]
            }
            data, err = hex.DecodeString(w[1][wCounter-3])
            if(len(temp[1]) == 1){
                temp[1] = "0" + temp[1]
            }
            data2, err = hex.DecodeString(temp[1])
            if err != nil {
                    panic(err)
            }
            w[1][wCounter+1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)


            if(len(w[2][wCounter-3]) == 1){
               w[2][wCounter-3] = "0" + w[2][wCounter-3]
            }
            data, err = hex.DecodeString(w[2][wCounter-3])
            if(len(temp[2]) == 1){
                 temp[2] = "0" + temp[2]
            }
            data2, err = hex.DecodeString(temp[2])
            if err != nil {
                panic(err)
            }
            w[2][wCounter+1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[3][wCounter-3]) == 1){
               w[3][wCounter-3] = "0" + w[3][wCounter-3]
            }
            data, err = hex.DecodeString(w[3][wCounter-3])
            if(len(temp[3]) == 1){
                temp[3] = "0" + temp[3]
            }
            data2, err = hex.DecodeString(temp[3])
            if err != nil {
                panic(err)
            }
            w[3][wCounter+1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            //block 2
            if(len(w[0][wCounter-2]) == 1){
                 w[0][wCounter-2] = "0" + w[0][wCounter-2]
            }
            data, err = hex.DecodeString(w[0][wCounter-2])
            if(len(w[0][wCounter-2]) == 1){
                w[0][wCounter+1] = "0" + w[0][wCounter+1]
            }
            data2, err = hex.DecodeString(w[0][wCounter+1])
            if err != nil {
                panic(err)
            }
            w[0][wCounter+2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[1][wCounter-2]) == 1){
                w[1][wCounter-2] = "0" + w[1][wCounter-2]
            }
            data, err = hex.DecodeString(w[1][wCounter-2])

            if(len(w[1][wCounter+1]) == 1){
                w[1][wCounter+1] = "0" + w[1][wCounter+1]
            }
            data2, err = hex.DecodeString(w[1][wCounter+1]) //this is empty for some reason
            //fmt.Println(w[1][wCounter+1])
            if err != nil {
                panic(err)
            }
            w[1][wCounter+2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[2][wCounter-2]) == 1){
                w[2][wCounter-2] = "0" + w[2][wCounter-2]
            }
            data, err = hex.DecodeString(w[2][wCounter-2])
            if(len(w[2][wCounter+1]) == 1){
                w[2][wCounter+1] = "0" + w[2][wCounter+1]
            }
            data2, err = hex.DecodeString(w[2][wCounter+1])
            if err != nil {
                panic(err)
            }
            w[2][wCounter+2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[3][wCounter-2]) == 1){
                w[3][wCounter-2] = "0" + w[3][wCounter-2]
            }
            data, err = hex.DecodeString(w[3][wCounter-2])
            if(len(w[3][wCounter+1]) == 1){
                 w[3][wCounter+1] = "0" + w[3][wCounter+1]
            }
            data2, err = hex.DecodeString(w[3][wCounter+1])
            if err != nil {
                panic(err)
            }
            w[3][wCounter+2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            //block 3
            if(len(w[0][wCounter-1]) == 1){
                w[0][wCounter-1] = "0" + w[0][wCounter-1]
            }
            data, err = hex.DecodeString(w[0][wCounter-1])
            if(len(w[0][wCounter+2]) == 1){
                w[0][wCounter+2] = "0" + w[0][wCounter+2]
            }
            data2, err = hex.DecodeString(w[0][wCounter+2])
            if err != nil {
                panic(err)
            }
            w[0][wCounter+3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[1][wCounter-1]) == 1){
                w[1][wCounter-1] = "0" + w[1][wCounter-1]
            }
            data, err = hex.DecodeString(w[1][wCounter-1])
            if(len(w[1][wCounter+2]) == 1){
                w[1][wCounter+2] = "0" + w[1][wCounter+2]
            }
            data2, err = hex.DecodeString(w[1][wCounter+2])
            if err != nil {
                panic(err)
            }
            w[1][wCounter+3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[2][wCounter-1]) == 1){
                w[2][wCounter-1] = "0" + w[2][wCounter-1]
            }
            data, err = hex.DecodeString(w[2][wCounter-1])
            if(len(w[2][wCounter+2]) == 1){
                 w[2][wCounter+2] = "0" + w[2][wCounter+2]
            }
            data2, err = hex.DecodeString(w[2][wCounter+2])
            if err != nil {
                panic(err)
            }
            w[2][wCounter+3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[3][wCounter-1]) == 1){
                w[3][wCounter-1] = "0" + w[3][wCounter-1]
            }
            data, err = hex.DecodeString(w[3][wCounter-1])
            if(len(w[3][wCounter+2]) == 1){
                w[3][wCounter+2] = "0" + w[3][wCounter+2]
            }
            data2, err = hex.DecodeString(w[3][wCounter+2])
            if err != nil {
                panic(err)
            }
            w[3][wCounter+3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            //block 4
            if(len(w[0][wCounter]) == 1){
                w[0][wCounter] = "0" + w[0][wCounter]
            }
            data, err = hex.DecodeString(w[0][wCounter])
            if(len(w[0][wCounter+3]) == 1){
                 w[0][wCounter+3] = "0" +w[0][wCounter+3]
            }
            data2, err = hex.DecodeString(w[0][wCounter+3])
            if err != nil {
                panic(err)
            }
            w[0][wCounter+4] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[1][wCounter]) == 1){
                w[1][wCounter] = "0" + w[1][wCounter]
            }
            data, err = hex.DecodeString(w[1][wCounter])
            if(len(w[1][wCounter+3]) == 1){
                 w[1][wCounter+3] = "0" +w[1][wCounter+3]
            }
            data2, err = hex.DecodeString(w[1][wCounter+3])
            if err != nil {
                panic(err)
            }
            w[1][wCounter+4] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[2][wCounter]) == 1){
                w[2][wCounter] = "0" + w[2][wCounter]
            }
            data, err = hex.DecodeString(w[2][wCounter])
            if(len(w[2][wCounter+3]) == 1){
                 w[2][wCounter+3] = "0" +w[2][wCounter+3]
            }
            data2, err = hex.DecodeString(w[2][wCounter+3])
            if err != nil {
                 panic(err)
            }
            w[2][wCounter+4] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

            if(len(w[3][wCounter]) == 1){
                w[3][wCounter] = "0" + w[3][wCounter]
            }
            data, err = hex.DecodeString(w[3][wCounter])
            if(len(w[3][wCounter+3]) == 1){
                w[3][wCounter+3] = "0" +w[3][wCounter+3]
            }
            data2, err = hex.DecodeString(w[3][wCounter+3])
            if err != nil {
                panic(err)
            }
            w[3][wCounter+4] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
            //end the calculations for the w columns

            //concatenate the rows of w calculated above to get the new key (loops 4x)
            for(helper < 5){//??
                //if it is null to start, don't add, simply set it or code will add the value to NULL
                //get value at w[0]
                if(len(w[0][wCounter + helper]) != 1 && roundKeysHex[i] == "") {
                    roundKeysHex[i] = strings.ToUpper(w[0][wCounter + helper])
                }else if(len(w[0][wCounter + helper]) == 1 && roundKeysHex[i] == ""){
                    roundKeysHex[i] = "0" + strings.ToUpper(w[0][wCounter + helper])
                    //w[0][wCounter + helper] = "0x0" + Integer.toHexString(Integer.decode(w[0][wCounter + helper]))
                }else if(len(w[0][wCounter + helper]) != 1){
                    roundKeysHex[i] += strings.ToUpper(w[0][wCounter + helper])
                    //if the length is not 2, it needs a leading 0
                } else {
                    roundKeysHex[i] +=  "0" + strings.ToUpper(w[0][wCounter + helper])
                   // w[0][wCounter + helper] = "0x0" + Integer.toHexString(Integer.decode(w[0][wCounter + helper]))
                }

                //get value at w[1]
                if(len(w[1][wCounter + helper]) != 1){
                    roundKeysHex[i] += strings.ToUpper(w[1][wCounter + helper])
                    //if the length is not 2, it needs a leading 0
                } else {
                    roundKeysHex[i] += "0" + strings.ToUpper(w[1][wCounter + helper])
                    //w[1][wCounter + helper] = "0x0" + Integer.toHexString(Integer.decode(w[1][wCounter + helper]))
                }

                //get value at w[2]
                if(len(w[2][wCounter + helper]) != 1){
                    roundKeysHex[i] += strings.ToUpper(w[2][wCounter + helper])
                    //if the length is not 2, it needs a leading 0
                } else {
                    roundKeysHex[i] += "0" + strings.ToUpper(w[2][wCounter + helper])
                    //w[2][wCounter + helper] = "0x0" + Integer.toHexString(Integer.decode(w[2][wCounter + helper]))
                }

                //get value at w[3]
                if(len(w[3][wCounter + helper]) != 1){
                    roundKeysHex[i] += strings.ToUpper(w[3][wCounter + helper])
                    //if the length is not 2, it needs a leading 0
                } else {
                    roundKeysHex[i] += "0" + strings.ToUpper(w[3][wCounter + helper])
                    //w[2][wCounter + helper] = "0x0" + Integer.toHexString(Integer.decode(w[2][wCounter + helper]))
                }
                helper++
            }

            //reset counters
            wCounter += 4
            helper = 1
        }
        return roundKeysHex;
}

// aesSBox
// This method contains the SBOX which is used to substitute bytes passed in from the algorithm
// param: string with the hex to be substituted
// return: string with the new hex value
func aesSBox(inHex string) string{
     var S_BOX = []string {"63" ,"7c" ,"77" ,"7b" ,"f2" ,"6b" ,"6f" ,"c5" ,"30" ,"01" ,"67" ,"2b" ,"fe" ,"d7" ,"ab" ,"76",
                        "ca" ,"82" ,"c9" ,"7d" ,"fa" ,"59" ,"47" ,"f0" ,"ad" ,"d4" ,"a2" ,"af" ,"9c" ,"a4" ,"72" ,"c0",
                        "b7" ,"fd" ,"93" ,"26" ,"36" ,"3f" ,"f7" ,"cc" ,"34" ,"a5" ,"e5" ,"f1" ,"71" ,"d8" ,"31" ,"15",
                        "04" ,"c7" ,"23" ,"c3" ,"18" ,"96" ,"05" ,"9a" ,"07" ,"12" ,"80" ,"e2" ,"eb" ,"27" ,"b2" ,"75",
                        "09" ,"83" ,"2c" ,"1a" ,"1b" ,"6e" ,"5a" ,"a0" ,"52" ,"3b" ,"d6" ,"b3" ,"29" ,"e3" ,"2f" ,"84",
                        "53" ,"d1" ,"00" ,"ed" ,"20" ,"fc" ,"b1" ,"5b" ,"6a" ,"cb" ,"be" ,"39" ,"4a" ,"4c" ,"58" ,"cf",
                        "d0" ,"ef" ,"aa" ,"fb" ,"43" ,"4d" ,"33" ,"85" ,"45" ,"f9" ,"02" ,"7f" ,"50" ,"3c" ,"9f" ,"a8",
                        "51" ,"a3" ,"40" ,"8f" ,"92" ,"9d" ,"38" ,"f5" ,"bc" ,"b6" ,"da" ,"21" ,"10" ,"ff" ,"f3" ,"d2",
                        "cd" ,"0c" ,"13" ,"ec" ,"5f" ,"97" ,"44" ,"17" ,"c4" ,"a7" ,"7e" ,"3d" ,"64" ,"5d" ,"19" ,"73",
                        "60" ,"81" ,"4f" ,"dc" ,"22" ,"2a" ,"90" ,"88" ,"46" ,"ee" ,"b8" ,"14" ,"de" ,"5e" ,"0b" ,"db",
                        "e0" ,"32" ,"3a" ,"0a" ,"49" ,"06" ,"24" ,"5c" ,"c2" ,"d3" ,"ac" ,"62" ,"91" ,"95" ,"e4" ,"79",
                        "e7" ,"c8" ,"37" ,"6d" ,"8d" ,"d5" ,"4e" ,"a9" ,"6c" ,"56" ,"f4" ,"ea" ,"65" ,"7a" ,"ae" ,"08",
                        "ba" ,"78" ,"25" ,"2e" ,"1c" ,"a6" ,"b4" ,"c6" ,"e8" ,"dd" ,"74" ,"1f" ,"4b" ,"bd" ,"8b" ,"8a",
                        "70" ,"3e" ,"b5" ,"66" ,"48" ,"03" ,"f6" ,"0e" ,"61" ,"35" ,"57" ,"b9" ,"86" ,"c1" ,"1d" ,"9e",
                        "e1" ,"f8" ,"98" ,"11" ,"69" ,"d9" ,"8e" ,"94" ,"9b" ,"1e" ,"87" ,"e9" ,"ce" ,"55" ,"28" ,"df",
                        "8c" ,"a1" ,"89" ,"0d" ,"bf" ,"e6" ,"42" ,"68" ,"41" ,"99" ,"2d" ,"0f" ,"b0" ,"54" ,"bb" ,"16"}


        //decode the current hex passed in
        if(len(inHex) == 1){
            inHex = "0" + inHex
        }
        var index, err = hex.DecodeString(inHex)
        if err != nil {
            panic(err)
        }

        //find the value in the array and return it
        return  S_BOX[index[0]]
}

// aesRcon
// this method contains the round constant to be XOR with the temp value above
// param: int the current round of the encryption
// return: the string hex value that is the round constant
func aesRcon(round int) string{
         var R_CON = []string {
                "8D","01","02","04","08","10","20","40","80","1B","36","6C","D8","AB","4D","9A",
                "2F","5E","BC","63","C6","97","35","6A","D4","B3","7D","FA","EF","C5","91","39",
                "72","E4","D3","BD","61","C2","9F","25","4A","94","33","66","CC","83","1D","3A",
                "74","E8","CB","8D","01","02","04","08","10","20","40","80","1B","36","6C","D8",
                "AB","4D","9A","2F","5E","BC","63","C6","97","35","6A","D4","B3","7D","FA","EF",
                "C5","91","39","72","E4","D3","BD","61","C2","9F","25","4A","94","33","66","CC",
                "83","1D","3A","74","E8","CB","8D","01","02","04","08","10","20","40","80","1B",
                "36","6C","D8","AB","4D","9A","2F","5E","BC","63","C6","97","35","6A","D4","B3",
                "7D","FA","EF","C5","91","39","72","E4","D3","BD","61","C2","9F","25","4A","94",
                "33","66","CC","83","1D","3A","74","E8","CB","8D","01","02","04","08","10","20",
                "40","80","1B","36","6C","D8","AB","4D","9A","2F","5E","BC","63","C6","97","35",
                "6A","D4","B3","7D","FA","EF","C5","91","39","72","E4","D3","BD","61","C2","9F",
                "25","4A","94","33","66","CC","83","1D","3A","74","E8","CB","8D","01","02","04",
                "08","10","20","40","80","1B","36","6C","D8","AB","4D","9A","2F","5E","BC","63",
                "C6","97","35","6A","D4","B3","7D","FA","EF","C5","91","39","72","E4","D3","BD",
                "61","C2","9F","25","4A","94","33","66","CC","83","1D","3A","74","E8","CB","8D"}

         //return the round constant to be used
        return R_CON[round]
}

// AESStateXOR
// this method performs the xor operation on two matrices(state and key). The first step of encryption process.
// param: state array, keyhex array
// return: string array that represents the new state array
func AESStateXOR(sHex [4][44]string, keyHex [4][44]string) [4][44]string {
        var outStateHex [4][44]string

        //xor all of the values
        data, err := hex.DecodeString(sHex[0][0])
        data2, err := hex.DecodeString(keyHex[0][0])
        if err != nil {
            panic(err)
        }
        outStateHex[0][0] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[0][1])
        data2, err = hex.DecodeString(keyHex[0][1])
        if err != nil {
            panic(err)
        }
        outStateHex[0][1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[0][2])
        data2, err = hex.DecodeString(keyHex[0][2])
        if err != nil {
            panic(err)
        }
        outStateHex[0][2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[0][3])
        data2, err = hex.DecodeString(keyHex[0][3])
        if err != nil {
            panic(err)
        }
        outStateHex[0][3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[1][0])
        data2, err = hex.DecodeString(keyHex[1][0])
        if err != nil {
             panic(err)
        }
        outStateHex[1][0] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[1][1])
        data2, err = hex.DecodeString(keyHex[1][1])
        if err != nil {
            panic(err)
        }
        outStateHex[1][1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[1][2])
        data2, err = hex.DecodeString(keyHex[1][2])
        if err != nil {
            panic(err)
        }
        outStateHex[1][2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[1][3])
        data2, err = hex.DecodeString(keyHex[1][3])
        if err != nil {
            panic(err)
        }
        outStateHex[1][3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[2][0])
        data2, err = hex.DecodeString(keyHex[2][0])
        if err != nil {
             panic(err)
        }
        outStateHex[2][0] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[2][1])
        data2, err = hex.DecodeString(keyHex[2][1])
        if err != nil {
            panic(err)
        }
        outStateHex[2][1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[2][2])
        data2, err = hex.DecodeString(keyHex[2][2])
        if err != nil {
            panic(err)
        }
        outStateHex[2][2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[2][3])
        data2, err = hex.DecodeString(keyHex[2][3])
        if err != nil {
            panic(err)
        }
        outStateHex[2][3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[3][0])
        data2, err = hex.DecodeString(keyHex[3][0])
        if err != nil {
            panic(err)
        }
        outStateHex[3][0] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[3][1])
        data2, err = hex.DecodeString(keyHex[3][1])
        if err != nil {
             panic(err)
        }
        outStateHex[3][1] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[3][2])
        data2, err = hex.DecodeString(keyHex[3][2])
        if err != nil {
            panic(err)
        }
        outStateHex[3][2] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)
        data, err = hex.DecodeString(sHex[3][3])
        data2, err = hex.DecodeString(keyHex[3][3])
        if err != nil {
            panic(err)
        }
        outStateHex[3][3] = strconv.FormatInt(int64(data[0] ^ data2[0]), 16)

        var col, row int
        //pad a value with zero if necessary
        for row = 0; row < len(outStateHex); row++ {
            for col = 0; col < len(outStateHex); col++ {
                if(len(outStateHex[row][col]) == 1){
                    outStateHex[row][col] = "0" + outStateHex[row][col]
                }
            }
        }
        return outStateHex;
}

// AESNibbleSub
// Uses the sBox method to subsitute all of the values in the state array. The second step in the encryption process.
// param: inStateHex, the current state array
// return: the updated state array after substitution
func AESNibbleSub(inStateHex [4][44]string) [4][44]string{
        var outStateHex [4][44]string

        var col, row int
        //loop through the array and swap out all of the values
        for row = 0; row < len(inStateHex); row++ {
            for col = 0; col < len(inStateHex); col++ {
                outStateHex[row][col] = aesSBox(inStateHex[row][col])
            }
        }
        return outStateHex;
}

// AESShiftRow
// This method shifts the rows of the state array. The first row remains the same, the second row shifts over 1,
// the third row shifts over 2, and the fourth row shifts over 3. The third step in encryption process
// param: string [] that is the current state array
// return: string array with the updated version of the state array after the shift
func AESShiftRow(inStateHex [4][44]string) [4][44]string{
    var temp []string = make([]string, 4)
    var outStateHex [4][44]string

    //first row is the same
    outStateHex[0][0] = inStateHex[0][0]
    outStateHex[0][1] = inStateHex[0][1]
    outStateHex[0][2] = inStateHex[0][2]
    outStateHex[0][3] = inStateHex[0][3]

    //shift the second row over 1
    temp[0] = inStateHex[1][0]
    temp[1] = inStateHex[1][1]
    temp[2] = inStateHex[1][2]
    temp[3] = inStateHex[1][3]

    outStateHex[1][0] = temp[1]
    outStateHex[1][1] = temp[2]
    outStateHex[1][2] = temp[3]
    outStateHex[1][3] = temp[0]

    //shift the third row over 2
    temp[0] = inStateHex[2][0]
    temp[1] = inStateHex[2][1]
    temp[2] = inStateHex[2][2]
    temp[3] = inStateHex[2][3]

    outStateHex[2][0] = temp[2]
    outStateHex[2][1] = temp[3]
    outStateHex[2][2] = temp[0]
    outStateHex[2][3] = temp[1]

    //shift the fourth row over 3
    temp[0] = inStateHex[3][0]
    temp[1] = inStateHex[3][1]
    temp[2] = inStateHex[3][2]
    temp[3] = inStateHex[3][3]

    outStateHex[3][0] = temp[3]
    outStateHex[3][1] = temp[0]
    outStateHex[3][2] = temp[1]
    outStateHex[3][3] = temp[2]

    return outStateHex
}

// AESMixColumn
// This method mixes the columns of the state array. It performs multiplication in the galois field. To simplify things,
// I have added MULT_TWO and MULT_THREE arrays, which substitutes the correct hex value in place of the multiplication.
// Xor operation is then performed on each row to complete the mix column. This is the last step in the encryption cycle.
// param: string[] that contains the current state array
// return: string[] that is the updated state array after mixing the columns
func AESMixColumn(inStateHex [4][44]string) [4][44]string{
    var outStateHex [4][44]string

    var MULT_TWO = []int {
                        0x00,0x02,0x04,0x06,0x08,0x0a,0x0c,0x0e,0x10,0x12,0x14,0x16,0x18,0x1a,0x1c,0x1e,
                        0x20,0x22,0x24,0x26,0x28,0x2a,0x2c,0x2e,0x30,0x32,0x34,0x36,0x38,0x3a,0x3c,0x3e,
                        0x40,0x42,0x44,0x46,0x48,0x4a,0x4c,0x4e,0x50,0x52,0x54,0x56,0x58,0x5a,0x5c,0x5e,
                        0x60,0x62,0x64,0x66,0x68,0x6a,0x6c,0x6e,0x70,0x72,0x74,0x76,0x78,0x7a,0x7c,0x7e,
                        0x80,0x82,0x84,0x86,0x88,0x8a,0x8c,0x8e,0x90,0x92,0x94,0x96,0x98,0x9a,0x9c,0x9e,
                        0xa0,0xa2,0xa4,0xa6,0xa8,0xaa,0xac,0xae,0xb0,0xb2,0xb4,0xb6,0xb8,0xba,0xbc,0xbe,
                        0xc0,0xc2,0xc4,0xc6,0xc8,0xca,0xcc,0xce,0xd0,0xd2,0xd4,0xd6,0xd8,0xda,0xdc,0xde,
                        0xe0,0xe2,0xe4,0xe6,0xe8,0xea,0xec,0xee,0xf0,0xf2,0xf4,0xf6,0xf8,0xfa,0xfc,0xfe,
                        0x1b,0x19,0x1f,0x1d,0x13,0x11,0x17,0x15,0x0b,0x09,0x0f,0x0d,0x03,0x01,0x07,0x05,
                        0x3b,0x39,0x3f,0x3d,0x33,0x31,0x37,0x35,0x2b,0x29,0x2f,0x2d,0x23,0x21,0x27,0x25,
                        0x5b,0x59,0x5f,0x5d,0x53,0x51,0x57,0x55,0x4b,0x49,0x4f,0x4d,0x43,0x41,0x47,0x45,
                        0x7b,0x79,0x7f,0x7d,0x73,0x71,0x77,0x75,0x6b,0x69,0x6f,0x6d,0x63,0x61,0x67,0x65,
                        0x9b,0x99,0x9f,0x9d,0x93,0x91,0x97,0x95,0x8b,0x89,0x8f,0x8d,0x83,0x81,0x87,0x85,
                        0xbb,0xb9,0xbf,0xbd,0xb3,0xb1,0xb7,0xb5,0xab,0xa9,0xaf,0xad,0xa3,0xa1,0xa7,0xa5,
                        0xdb,0xd9,0xdf,0xdd,0xd3,0xd1,0xd7,0xd5,0xcb,0xc9,0xcf,0xcd,0xc3,0xc1,0xc7,0xc5,
                        0xfb,0xf9,0xff,0xfd,0xf3,0xf1,0xf7,0xf5,0xeb,0xe9,0xef,0xed,0xe3,0xe1,0xe7,0xe5}

    var MULT_THREE = []int {
                        0x00,0x03,0x06,0x05,0x0c,0x0f,0x0a,0x09,0x18,0x1b,0x1e,0x1d,0x14,0x17,0x12,0x11,
                        0x30,0x33,0x36,0x35,0x3c,0x3f,0x3a,0x39,0x28,0x2b,0x2e,0x2d,0x24,0x27,0x22,0x21,
                        0x60,0x63,0x66,0x65,0x6c,0x6f,0x6a,0x69,0x78,0x7b,0x7e,0x7d,0x74,0x77,0x72,0x71,
                        0x50,0x53,0x56,0x55,0x5c,0x5f,0x5a,0x59,0x48,0x4b,0x4e,0x4d,0x44,0x47,0x42,0x41,
                        0xc0,0xc3,0xc6,0xc5,0xcc,0xcf,0xca,0xc9,0xd8,0xdb,0xde,0xdd,0xd4,0xd7,0xd2,0xd1,
                        0xf0,0xf3,0xf6,0xf5,0xfc,0xff,0xfa,0xf9,0xe8,0xeb,0xee,0xed,0xe4,0xe7,0xe2,0xe1,
                        0xa0,0xa3,0xa6,0xa5,0xac,0xaf,0xaa,0xa9,0xb8,0xbb,0xbe,0xbd,0xb4,0xb7,0xb2,0xb1,
                        0x90,0x93,0x96,0x95,0x9c,0x9f,0x9a,0x99,0x88,0x8b,0x8e,0x8d,0x84,0x87,0x82,0x81,
                        0x9b,0x98,0x9d,0x9e,0x97,0x94,0x91,0x92,0x83,0x80,0x85,0x86,0x8f,0x8c,0x89,0x8a,
                        0xab,0xa8,0xad,0xae,0xa7,0xa4,0xa1,0xa2,0xb3,0xb0,0xb5,0xb6,0xbf,0xbc,0xb9,0xba,
                        0xfb,0xf8,0xfd,0xfe,0xf7,0xf4,0xf1,0xf2,0xe3,0xe0,0xe5,0xe6,0xef,0xec,0xe9,0xea,
                        0xcb,0xc8,0xcd,0xce,0xc7,0xc4,0xc1,0xc2,0xd3,0xd0,0xd5,0xd6,0xdf,0xdc,0xd9,0xda,
                        0x5b,0x58,0x5d,0x5e,0x57,0x54,0x51,0x52,0x43,0x40,0x45,0x46,0x4f,0x4c,0x49,0x4a,
                        0x6b,0x68,0x6d,0x6e,0x67,0x64,0x61,0x62,0x73,0x70,0x75,0x76,0x7f,0x7c,0x79,0x7a,
                        0x3b,0x38,0x3d,0x3e,0x37,0x34,0x31,0x32,0x23,0x20,0x25,0x26,0x2f,0x2c,0x29,0x2a,
                        0x0b,0x08,0x0d,0x0e,0x07,0x04,0x01,0x02,0x13,0x10,0x15,0x16,0x1f,0x1c,0x19,0x1a}

    //first column
     data, err := hex.DecodeString(inStateHex[0][0])
     data2, err := hex.DecodeString(inStateHex[1][0])
     data3, err := hex.DecodeString(inStateHex[2][0])
     data4, err := hex.DecodeString(inStateHex[3][0])
     if err != nil {
         panic(err)
     }
    outStateHex[0][0] = strconv.FormatInt(int64(MULT_TWO[int(data[0])] ^ MULT_THREE[int(data2[0])] ^ int(data3[0]) ^ int(data4[0])), 16)
    outStateHex[1][0] = strconv.FormatInt(int64(int(data[0]) ^ MULT_TWO[int(data2[0])] ^ MULT_THREE[int(data3[0])] ^ int(data4[0])), 16)
    outStateHex[2][0] = strconv.FormatInt(int64(int(data[0]) ^ int(data2[0]) ^ MULT_TWO[int(data3[0])] ^ MULT_THREE[int(data4[0])]), 16)
    outStateHex[3][0] = strconv.FormatInt(int64(MULT_THREE[int(data[0])] ^ int(data2[0]) ^ int(data3[0]) ^ MULT_TWO[int(data4[0])]), 16)

    //second column
    data, err = hex.DecodeString(inStateHex[0][1])
    data2, err = hex.DecodeString(inStateHex[1][1])
    data3, err = hex.DecodeString(inStateHex[2][1])
    data4, err = hex.DecodeString(inStateHex[3][1])
    if err != nil {
        panic(err)
    }
    outStateHex[0][1] = strconv.FormatInt(int64(MULT_TWO[int(data[0])] ^ MULT_THREE[int(data2[0])] ^ int(data3[0]) ^ int(data4[0])), 16)
    outStateHex[1][1] = strconv.FormatInt(int64(int(data[0]) ^ MULT_TWO[int(data2[0])] ^ MULT_THREE[int(data3[0])] ^ int(data4[0])), 16)
    outStateHex[2][1] = strconv.FormatInt(int64(int(data[0]) ^ int(data2[0]) ^ MULT_TWO[int(data3[0])] ^ MULT_THREE[int(data4[0])]), 16)
    outStateHex[3][1] = strconv.FormatInt(int64(MULT_THREE[int(data[0])] ^ int(data2[0]) ^ int(data3[0]) ^ MULT_TWO[int(data4[0])]), 16)

    //third column
    data, err = hex.DecodeString(inStateHex[0][2])
    data2, err = hex.DecodeString(inStateHex[1][2])
    data3, err = hex.DecodeString(inStateHex[2][2])
    data4, err = hex.DecodeString(inStateHex[3][2])
    if err != nil {
        panic(err)
    }
    outStateHex[0][2] = strconv.FormatInt(int64(MULT_TWO[int(data[0])] ^ MULT_THREE[int(data2[0])] ^ int(data3[0]) ^ int(data4[0])), 16)
    outStateHex[1][2] = strconv.FormatInt(int64(int(data[0]) ^ MULT_TWO[int(data2[0])] ^ MULT_THREE[int(data3[0])] ^ int(data4[0])), 16)
    outStateHex[2][2] = strconv.FormatInt(int64(int(data[0]) ^ int(data2[0]) ^ MULT_TWO[int(data3[0])] ^ MULT_THREE[int(data4[0])]), 16)
    outStateHex[3][2] = strconv.FormatInt(int64(MULT_THREE[int(data[0])] ^ int(data2[0]) ^ int(data3[0]) ^ MULT_TWO[int(data4[0])]), 16)

    //fourth column
    data, err = hex.DecodeString(inStateHex[0][3])
    data2, err = hex.DecodeString(inStateHex[1][3])
    data3, err = hex.DecodeString(inStateHex[2][3])
    data4, err = hex.DecodeString(inStateHex[3][3])
    if err != nil {
        panic(err)
    }
    outStateHex[0][3] = strconv.FormatInt(int64(MULT_TWO[int(data[0])] ^ MULT_THREE[int(data2[0])] ^ int(data3[0]) ^ int(data4[0])), 16)
    outStateHex[1][3] = strconv.FormatInt(int64(int(data[0]) ^ MULT_TWO[int(data2[0])] ^ MULT_THREE[int(data3[0])] ^ int(data4[0])), 16)
    outStateHex[2][3] = strconv.FormatInt(int64(int(data[0]) ^ int(data2[0]) ^ MULT_TWO[int(data3[0])] ^ MULT_THREE[int(data4[0])]), 16)
    outStateHex[3][3] = strconv.FormatInt(int64(MULT_THREE[int(data[0])] ^ int(data2[0]) ^ int(data3[0]) ^ MULT_TWO[int(data4[0])]), 16)

   var row, col int
    //pad with zeros if necessary
    for row = 0; row < len(outStateHex); row++ {
        for col = 0; col < len(outStateHex); col++ {
            if(len(outStateHex[row][col]) == 1){
                outStateHex[row][col] = "0" + outStateHex[row][col]
            }
        }
    }
    return outStateHex
}