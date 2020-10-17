## Seam Carving
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row; a horizontal seam is a path of pixels connected from the left to the right with one pixel in each column. Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest features (aspect ratio, set of objects present, etc.) of the image.

Although the [underlying algorithm](https://www.youtube.com/watch?v=6NcIJXTlugc&ab_channel=r3dux) is simple and elegant, it was not discovered until 2007. Now, it is now a core feature in Adobe Photoshop and other computer graphics applications.

For more information, check the [official assignment description](https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php)

In this assignment, you will create a data type that resizes a W-by-H image using the seam-carving technique.

## How to compile
Linux / Windows
```
javac -cp ../../lib/algs4.jar SeamCarver.java
```