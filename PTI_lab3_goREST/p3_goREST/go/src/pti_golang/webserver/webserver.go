package main

import (
		"fmt"
		"log"
		"net/http"
		"github.com/gorilla/mux"
		"encoding/json"
	"io"
	"io/ioutil"
	"encoding/csv"
	"os"
	"bufio"
	"strconv"
)

type ResponseMessage struct {
    Make string
    Model string
    Nofdays int
    Nofunits int
    Price int
}

type RequestMessage struct {
    Make string
    Model string
    Nofdays int
    Nofunits int
}


func main() {

router := mux.NewRouter().StrictSlash(true)
router.HandleFunc("/", Index)

//carrental functions
router.HandleFunc("/newrental/", newCarRental)
router.HandleFunc("/listrentals/", listRentals)


log.Fatal(http.ListenAndServe(":8080", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
}


func writeToFile(w http.ResponseWriter, NewMake string, NewModel string, NewNofdays int, NewNofunits int) {
    file, err := os.OpenFile("rentals.csv", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
    }
    writer := csv.NewWriter(file)
    var data1 = []string{NewMake, NewModel, strconv.Itoa(NewNofdays), strconv.Itoa(NewNofunits)}
    writer.Write(data1)
    writer.Flush()
    file.Close()
}


func newCarRental(w http.ResponseWriter, r *http.Request) {
	
	var requestMessage RequestMessage
    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    if err := json.Unmarshal(body, &requestMessage); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    } else {
		
        price := (requestMessage.Nofdays * 10) + (requestMessage.Nofunits * 20)
        
        
        /*res := ResponseMessage { Make: requestMessage.Make, Model: requestMessage.Model, 
								Nofdays: requestMessage.Nofdays, Nofunits: requestMessage.Nofunits,
							    Price: price } 
		*/
		
		fmt.Println("Resumen de compra\n")
		fmt.Print("Marca: ")
		fmt.Println(requestMessage.Make)
		fmt.Print("Model: ")
		fmt.Println(requestMessage.Model)
		fmt.Print("Numero de dias: ")
		fmt.Println(requestMessage.Nofdays)
		fmt.Print("Numero de unidades: ")
		fmt.Println(requestMessage.Nofunits)
		fmt.Println("--------------------------------------------------")
		fmt.Print("Total: ")
		fmt.Println(price)					    
        
        writeToFile(w, requestMessage.Make, requestMessage.Model, requestMessage.Nofdays, requestMessage.Nofunits)

		}
	}
	
func listRentals(w http.ResponseWriter, r *http.Request) {
	
	file, err := os.Open("rentals.csv")    
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
    }
    reader := csv.NewReader(bufio.NewReader(file))
    for {
        record, err := reader.Read()
        if err == io.EOF {
            break
        }

        fmt.Fprintf(w, "The rental is: %q \n",record)
		}
	}
	




