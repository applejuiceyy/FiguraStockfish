a few warning labels in here

* Even though Stockfish is very prominent thorough the title and code, it should support any UCI program, the Stockfish name is due to being easier to reference and more impressive than just calling it "FiguraUCI"
* hardcoded paths all around; this was a semi-private project and such there was no effort to customize it nor to avoid bad mod configs
* it doesn't implement the entirety of the UCI protocol nor am I experienced with chess engines to implement a worthy UCI protocol, this implementation is just from my interpretation of the specification (which is just 1 txt file), however I tried to make it not make assumptions that didn't exist on the specifications

other than that, welcome I guess