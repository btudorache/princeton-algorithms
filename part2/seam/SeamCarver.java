import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture currPicture;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.currPicture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(this.currPicture);
    }

    public int width() {
        return this.currPicture.width();
    }

    public int height() {
        return this.currPicture.height();
    }

    private double getDeltaXSquared(int x, int y) {
        int color1 = this.currPicture.get(y, x + 1).getRGB();
        int color2 = this.currPicture.get(y, x - 1).getRGB();

        int R1 = (color1 >> 16) & 0xFF;
        int G1 = (color1 >> 8) & 0xFF;
        int B1 = (color1) & 0xFF;

        int R2 = (color2 >> 16) & 0xFF;
        int G2 = (color2 >> 8) & 0xFF;
        int B2 = (color2) & 0xFF;

        return ((R1 - R2) * (R1 - R2) + (G1 - G2) * (G1 - G2) + (B1 - B2) * (B1 - B2));
    }

    private double getDeltaYSquared(int x, int y) {
        int color1 = this.currPicture.get(y + 1, x).getRGB();
        int color2 = this.currPicture.get(y - 1, x).getRGB();

        int R1 = (color1 >> 16) & 0xFF;
        int G1 = (color1 >> 8) & 0xFF;
        int B1 = (color1) & 0xFF;

        int R2 = (color2 >> 16) & 0xFF;
        int G2 = (color2 >> 8) & 0xFF;
        int B2 = (color2) & 0xFF;

        return ((R1 - R2) * (R1 - R2) + (G1 - G2) * (G1 - G2) + (B1 - B2) * (B1 - B2));
    }

    public double energy(int y, int x) {
        if (x < 0 || x > this.height() - 1 || y < 0 || y > this.width() - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == this.height() - 1 || y == 0 || y == this.width() - 1) {
            return 1000.00;
        }
        else {
            return Math.sqrt(this.getDeltaXSquared(x, y) + this.getDeltaYSquared(x, y));
        }
    }

    private double[][] getEnergyMatrix() {
        double[][] energyMatrix = new double[this.height()][this.width()];
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                energyMatrix[i][j] = energy(j, i);
            }
        }
        return energyMatrix;
    }

    public int[] findVerticalSeam() {
        double[][] energyMatrix = getEnergyMatrix();
        double[][] dp = new double[height()][width()];
        int[][] parents = new int[height()][width()];

        for (int j = 0; j < this.width(); j++) {
            dp[0][j] = energyMatrix[0][j];
        }

        if (height() > 1 && width() > 1) {
            for (int i = 1; i < this.height(); i++) {
                for (int j = 0; j < this.width(); j++) {
                    int start;
                    int end;

                    if (j == 0) {
                        start = 0;
                        end = 1;
                    }
                    else if (j == this.width() - 1) {
                        start = this.width() - 2;
                        end = this.width() - 1;
                    }
                    else {
                        start = j - 1;
                        end = j + 1;
                    }

                    int y = -1;
                    double minVal = Double.POSITIVE_INFINITY;
                    for (int k = start; k <= end; k++) {
                        if (dp[i - 1][k] < minVal) {
                            minVal = dp[i - 1][k];
                            y = k;
                        }
                    }

                    parents[i][j] = y;
                    dp[i][j] += (energyMatrix[i][j] + minVal);
                }
            }
        }

        double minSeam = Double.POSITIVE_INFINITY;
        int y = -1;
        for (int j = 0; j < this.width(); j++) {
            if (dp[this.height() - 1][j] < minSeam) {
                minSeam = dp[this.height() - 1][j];
                y = j;
            }
        }

        int[] path = new int[height()];
        path[this.height() - 1] = y;
        int parent = parents[this.height() - 1][y];

        for (int i = this.height() - 2; i >= 0; i--) {
            path[i] = parent;
            parent = parents[i][parent];
        }

        return path;
    }

    private void transpose() {
        Picture newPicture = new Picture(this.height(), this.width());
        for (int i = 0; i < newPicture.width(); i++) {
            for (int j = 0; j < newPicture.height(); j++) {
                newPicture.set(i, j, this.currPicture.get(j, i));
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
                newPicture.set(newPicY, newPicX, this.currPicture.get(j, i));
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
        sc.findVerticalSeam();
        sc.findHorizontalSeam();
    }
}