## Burrows–Wheeler
Implement the Burrows–Wheeler data compression algorithm. This revolutionary algorithm outcompresses gzip and PKZIP, is relatively easy to implement, and is not protected by any patents. It forms the basis of the Unix compression utility [bzip2](http://www.bzip.org/).

The Burrows–Wheeler data compression algorithm consists of three algorithmic components, which are applied in succession:

1. *Burrows–Wheeler transform.* Given a typical English text file, transform it into a text file in which sequences of the same character occur near each other many times.
2. *Move-to-front encoding.* Given a text file in which sequences of the same character occur near each other many times, convert it into a text file in which certain characters appear much more frequently than others.
3. *Huffman compression.* Given a text file in which certain characters appear much more frequently than others, compress it by encoding frequently occurring characters with short codewords and infrequently occurring characters with long codewords.
Step 3 is the only one that compresses the message: it is particularly effective because Steps 1 and 2 produce a text file in which certain characters appear much more frequently than others. To expand a message, apply the inverse operations in reverse order: first apply the Huffman expansion, then the move-to-front decoding, and finally the inverse Burrows–Wheeler transform. Your task is to implement the Burrows–Wheeler and move-to-front components.

For more information, check the [official assignment description](https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php)

## How to compile
Linux / Windows
```
javac -cp ../../lib/algs4.jar BurrowsWheeler.java CircularSuffixArray.java MoveToFront.java
```