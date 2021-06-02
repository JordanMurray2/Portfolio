/**
 * file       : main.go
 * author     : Jordan Murray
 * course     : CMPT 630
 * assignment : Final Project
 * due date   : May 23, 2021
 * version    : 1.0
 */
package main

import (
	"fmt"
	"html/template"
	"log"
	"net/http"
    "io/ioutil"
    "encoding/hex"
)

// Load the index.html template.
var tmpl = template.Must(template.New("tmpl").ParseFiles("../client/index.html"))

func main() {
	// Serve / with the index.html file.
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		if err := tmpl.ExecuteTemplate(w, "index.html", nil); err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	})

	// Serve /callme with the encrypted response.
	http.HandleFunc("/callme", func(w http.ResponseWriter, r *http.Request) {
	    //parse the form
        r.ParseMultipartForm(10 << 20)
        var file, handler, err = r.FormFile("myfile")
        fmt.Println(handler.Size)
        if err != nil {
            fmt.Println("Error Retrieving the File")
            fmt.Println(err)
            return
        }
        defer file.Close()

        // ioutil.ReadAll() will read every byte
        // from the reader (in this case a file),
        // and return a slice of unknown slice
        data, err := ioutil.ReadAll(file)
        if err != nil {
            log.Fatal(err)
        }

        //convert the file content to hex string
        var content string =  hex.EncodeToString(data)

        //begin the AES encryption
        var result string = parseFile(content)

        //Print the result of the AES cipher onto the webpage
		fmt.Fprintln(w, result)
	})

	// Start the server at http://localhost:9000
	log.Fatal(http.ListenAndServe(":9000", nil))
}