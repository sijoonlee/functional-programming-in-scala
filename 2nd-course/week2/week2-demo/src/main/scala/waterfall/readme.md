## States and Moves

Glass: Int
State: Vector[Int] (one entry per glass)
Moves:
    Empty(glass)
    Fill(glass)
    Pour(from, to)
    Paths - length 1, length 2, length 3, ...
    
## Example

Glass 0 : capacity 4
Glass 1 : capacity 9

[0,0]  -> fill 0 -> [4,0] -> pour 0, 1 -> [0,4]

[0,0]  -> fill 1 -> [0,9] -> pour 1, 0 -> [4,5] -> empty 1 -> [4,0]