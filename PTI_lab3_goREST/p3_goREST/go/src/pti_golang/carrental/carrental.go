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
    "strconv"
    "bufio"

)

type ResponseMessage struct {
    Field1 string
    Field2 string
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
router.HandleFunc("/newrental",newCarRental)
router.HandleFunc("/listrentals/",listRentals)

log.Fatal(http.ListenAndServe(":8080", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
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
		newmake := requestMessage.Make
		newmodel := requestMessage.Model
		newnofdays := requestMessage.Nofdays
		newnofunits := requestMessage.Nofunits
		
		price := (newnofdays * 20) + (newnofunits * 10)
		fmt.Println("\nNew rental\n")
		fmt.Print("Make: ")
		fmt.Println(newmake)
		fmt.Print("Model: ")
		fmt.Println(newmodel)
		fmt.Print("Number of days: ")
		fmt.Println(newnofdays)
		fmt.Print("Number of units: ")
		fmt.Println(newnofunits)
		fmt.Println("--------------------------------------------------")
		fmt.Print("Total price: ")
		fmt.Println(price)
		writeToFile(w,newmake,newmodel,newnofdays,newnofunits)
		
	}
}

func listRentals(w http.ResponseWriter, r *http.Request){
	var rental RequestMessage
	var rentals []RequestMessage
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
        rental.Make = record[0]
        rental.Model = record[1]
        rental.Nofdays, _ = strconv.Atoi(record[2])
        rental.Nofunits, _ = strconv.Atoi(record[3])
        rentals = append(rentals, rental)
    }
		jsonData, err := json.Marshal(rentals)
		if err != nil {
			fmt.Println(err)
			os.Exit(1)
		}
		fmt.Fprintf(w, "%q\n", string(jsonData))
		
	
}

func writeToFile(w http.ResponseWriter, make string, model string, nofdays int, nofunits int) {
    file, err := os.OpenFile("rentals.csv", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
    }
    writer := csv.NewWriter(file)
    var data1 = []string{make,model,strconv.Itoa(nofdays),strconv.Itoa(nofunits)}
    writer.Write(data1)
    writer.Flush()
    file.Close()
}
