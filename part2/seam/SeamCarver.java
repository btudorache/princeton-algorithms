import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture currPicture;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.currPicture = picture;
    }

    public Picture picture() {
        return this.currPicture;
    }

    public int width() {
        return this.currPicture.width();
    }

    public int height() {
        return this.currPicture.height();
    }

    private double getDeltaXSquared(int x, int y) {
        int color1 = this.currPicture.get(x + 1, y).getRGB();
        int color2 = this.currPicture.get(x - 1, y).getRGB();

        int R1 = (color1 >> 16) & 0xFF;
        int G1 = (color1 >> 8) & 0xFF;
        int B1 = (color1 >> 0) & 0xFF;

        int R2 = (color2 >> 16) & 0xFF;
        int G2 = (color2 >> 8) & 0xFF;
        int B2 = (color2 >> 0) & 0xFF;

        return ((R1 - R2) * (R1 - R2) + (G1 - G2) * (G1 - G2) + (B1 - B2) * (B1 - B2));
    }

    private double getDeltaYSquared(int x, int y) {
        int color1 = this.currPicture.get(x, y + 1).getRGB();
        int color2 = this.currPicture.get(x, y - 1).getRGB();

        int R1 = (color1 >> 16) & 0xFF;
        int G1 = (color1 >> 8) & 0xFF;
        int B1 = (color1 >> 0) & 0xFF;

        int R2 = (color2 >> 16) & 0xFF;
        int G2 = (color2 >> 8) & 0xFF;
        int B2 = (color2 >> 0) & 0xFF;

        return ((R1 - R2) * (R1 - R2) + (G1 - G2) * (G1 - G2) + (B1 - B2) * (B1 - B2));
    }

    public double energy(int x, int y) {
        if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1) {
            return 1000.00;
        }
        else {
            return Math.sqrt(this.getDeltaXSquared(x, y) + this.getDeltaYSquared(x, y));
        }
    }

    private double[][] getEnergyMatrix() {
        double[][] energyMatrix = new double[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                energyMatrix[i][j] = energy(i, j);
            }
        }
        return energyMatrix;
    }

    private class Node {
        private int x;
        private int y;
        private Node prevNode;
        private double weight;

        private Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.prevNode = null;
            this.weight = Double.POSITIVE_INFINITY;
        }

        private Node(int x, int y, double weight) {
            this.x = x;
            this.y = y;
            this.prevNode = null;
            this.weight = weight;
        }

        private double getWeight() {
            return this.weight;
        }

        private void setWeight(double weight) {
            this.weight = weight;
        }

        private void setPrevNode(Node prevNode) {
            this.prevNode = prevNode;
        }

        private int getX() {
            return this.x;
        }
    }

    private void relaxe(int x, int y, double[][] energyMatrix, Node[][] nodes) {
        if (y + 1 > this.height() - 1) {
            return;
        }
        double checkWeight;
        for (int i = -1; i <= 1; i++) {
            if (x + i < 0 || x + i > this.width() - 1) {
                continue;
            }
            checkWeight = nodes[x][y].getWeight() + energyMatrix[x + i][y + 1];
            if (checkWeight < nodes[x + i][y + 1].getWeight()) {
                nodes[x + i][y + 1].setWeight(checkWeight);
                nodes[x + i][y + 1].setPrevNode(nodes[x][y]);
            }
        }
    }

    public int[] findVerticalSeam() {
        double[][] energyMatrix = getEnergyMatrix();
        Node[][] nodes;
        double min = Double.POSITIVE_INFINITY;
        double localMin;
        int[] path = new int[this.height()];

        for (int i = 0; i < this.width(); i++) {

            // initialize nodes
            nodes = new Node[this.width()][this.height()];
            for (int j = 0; j < this.height(); j++) {
                int start;
                int finish;
                if (i - j < 0) {
                    start = 0;
                }
                else {
                    start = i - j;
                }
                if (i + j > this.width() - 1) {
                    finish = this.width() - 1;
                }
                else {
                    finish = i + j;
                }
                for (int k = start; k <= finish; k++) {
                    if (j == 0) {
                        nodes[k][j] = new Node(k, j, 1000.00);
                    }
                    else {
                        nodes[k][j] = new Node(k, j);
                    }
                }
            }

            // relaxe nodes
            for (int j = 0; j < this.height(); j++) {
                int start;
                int finish;
                if (i - j < 0) {
                    start = 0;
                }
                else {
                    start = i - j;
                }
                if (i + j > this.width() - 1) {
                    finish = this.width() - 1;
                }
                else {
                    finish = i + j;
                }
                for (int k = start; k <= finish; k++) {
                    relaxe(k, j, energyMatrix, nodes);
                }
            }

            localMin = Double.POSITIVE_INFINITY;
            Node minPath = null;
            for (int t = 0; t < this.width(); t++) {
                if (nodes[t][this.height() - 1] != null
                        && nodes[t][this.height() - 1].getWeight() < localMin) {
                    localMin = nodes[t][this.height() - 1].getWeight();
                    minPath = nodes[t][this.height() - 1];
                }
            }

            if (localMin < min) {
                int ind = this.height() - 1;
                for (Node node = minPath; node != null; node = node.prevNode) {
                    path[ind--] = node.getX();
                }
                min = localMin;
            }
        }
        return path;
    }

    private void transpose() {
        Picture newPicture = new Picture(this.height(), this.width());
        for (int i = 0; i < newPicture.width(); i++) {
            for (int j = 0; j < newPicture.height(); j++) {
                newPicture.set(i, j, this.picture().get(j, i));
            }
        }
        this.currPicture = newPicture;
    }

    public int[] findHorizontalSeam() {
        this.transpose();
        int[] path = this.findVerticalSeam();
        this.transpose();
        return path;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.height()) {
            throw new IllegalArgumentException();
        }
        if (seam[0] < 0 || seam[0] > this.width() - 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > this.width() - 1) {
                throw new IllegalArgumentException();
            }
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (this.width() <= 1) {
            throw new IllegalArgumentException();
        }

        Picture newPicture = new Picture(this.width() - 1, this.height());
        int newPicX, newPicY;
        for (int i = 0; i < this.height(); i++) {
            newPicX = i;
            newPicY = -1;
            for (int j = 0; j < this.width(); j++) {
                newPicY++;
                // skip seam column
                if (seam[i] == j) {
                    newPicY--;
                    continue;
                }
                newPicture.set(newPicY, newPicX, this.picture().get(j, i));
            }
        }
        this.currPicture = newPicture;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.width()) {
            throw new IllegalArgumentException();
        }
        if (seam[0] < 0 || seam[0] > this.height() - 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > this.height() - 1) {
                throw new IllegalArgumentException();
            }
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (this.height() <= 1) {
            throw new IllegalArgumentException();
        }

        this.transpose();
        this.removeVerticalSeam(seam);
        this.transpose();
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        sc.removeVerticalSeam(sc.findVerticalSeam());
    }
}
