import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.util.Vector;

public class Main {
    static Vector<Integer> avg(Vector<Vector<Integer>> vec) {
        Vector<Integer> retVec = new Vector<>();
        int[] sum = new int[vec.get(0).size()];
        for (Vector<Integer> v : vec) {
            for (int i = 0; i < v.size(); i++) {
                sum[i] += v.get(i);
            }
        }
        for (int i = 0; i < sum.length; i++) {
            retVec.add(sum[i] / vec.size());
        }
        return retVec;
    }

    static void quantize(Vector<Vector<Integer>> vec, Vector<Vector<Integer>> qun, int lvl) {
        if (lvl == 1 || vec.size() == 0) {
            if (vec.size() > 0) {
                qun.add(avg(vec));
            }
            return;
        }
        Vector<Vector<Integer>> l = new Vector<>(), r = new Vector<>();
        Vector<Integer> m = avg(vec);
        for (Vector<Integer> v : vec) {
            int dist1 = distance(v, m, 1);
            int dist2 = distance(v, m, - 1);
            if (dist1 <= dist2) {
                r.add(v);
            } else {
                l.add(v);
            }
        }
        quantize(l, qun, lvl / 2);
        quantize(r, qun, lvl / 2);
    }

    static int distance(Vector<Integer> x, Vector<Integer> y, int factor) {
        int dist = 0;
        for (int i = 0; i < x.size(); ++ i) {
            dist += Math.pow(x.get(i) - y.get(i) + factor, 2);
        }
        return (int) Math.sqrt(dist);
    }


    private static Vector<Integer> opt(Vector<Vector<Integer>> vec, Vector<Vector<Integer>> quantize) {
        Vector<Integer> VectorsToQuantizedIndices = new Vector<>();
        for (Vector<Integer> v : vec) {
            int smallestDistance = distance(v, quantize.get(0), 0);
            int smallestIndex = 0;
            for (int i = 1; i < quantize.size(); i++) {
                int tempDistance = distance(v, quantize.get(i), 0);
                if (tempDistance < smallestDistance) {
                    smallestDistance = tempDistance;
                    smallestIndex = i;
                }
            }
            VectorsToQuantizedIndices.add(smallestIndex);
        }
        return VectorsToQuantizedIndices;
    }


    static void compress(int vectorHeight, int vectorWidth, int codeBookSize,String s) throws IOException {
        Vector<Vector<Integer>> vectors = new Vector<>();
        Vector<Vector<Integer>> quantize = new Vector<>();
        FileOutputStream fileOutputStream = new FileOutputStream("compress");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        File file = new File(s);
        BufferedImage img = ImageIO.read(file);
        int orgWidth = img.getWidth();
        int orgHeight = ((BufferedImage) img).getHeight();
        int[][] imgArr = new int[orgHeight][orgWidth];
        Raster raster = img.getData();
        for (int i = 0; i < orgHeight; i++) {
            for (int j = 0; j < orgWidth; j++) {
                imgArr[i][j] = raster.getSample(j, i, 0);
            }
        }
        int sclWidth = orgWidth, sclHeight = orgHeight;
        if (orgHeight % vectorHeight != 0) {
            sclHeight = (sclHeight / vectorHeight) + 1;
            sclHeight *= vectorHeight;
        }
        if (orgWidth % vectorWidth != 0) {
            sclWidth = (sclWidth / vectorWidth) + 1;

            sclWidth *= vectorWidth;
        }
        int[][] sclImg = new int[sclHeight][sclWidth];
        for (int i = 0; i < sclHeight; i++) {
            int heightTemp = i;
            if (heightTemp >= orgHeight) {
                heightTemp = orgHeight - 1;
            }
            for (int j = 0; j < sclWidth; j++) {
                int widthTemp = j;
                if (widthTemp >= orgWidth) {
                    widthTemp = orgWidth - 1;
                }
                sclImg[i][j] = imgArr[heightTemp][widthTemp];
            }
        }
        for (int i = 0; i < sclHeight; i += vectorHeight) {
            for (int j = 0; j < sclWidth; j += vectorWidth) {
                vectors.add(new Vector<>());
                for (int x = i; x < i + vectorHeight; x++) {
                    for (int y = j; y < j + vectorWidth; y++) {
                        vectors.lastElement().add(sclImg[x][y]);
                    }
                }
            }
        }
        quantize(vectors, quantize, codeBookSize);
        Vector<Integer> VectorsToQuantizedIndices = opt(vectors, quantize);
        objectOutputStream.writeObject(orgWidth);
        objectOutputStream.writeObject(orgHeight);
        objectOutputStream.writeObject(sclWidth);
        objectOutputStream.writeObject(sclHeight);
        objectOutputStream.writeObject(vectorWidth);
        objectOutputStream.writeObject(vectorHeight);
        objectOutputStream.writeObject(VectorsToQuantizedIndices);
        objectOutputStream.writeObject(quantize);
        objectOutputStream.close();
    }

    public static BufferedImage getBufferedImage(int[][] imagePixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            int s;
            if (y == 199)
                s = 13;
            for (int x = 0; x < width; x++) {
                int value = - 1 << 24;
                value = 0xff000000 | (imagePixels[y][x] << 16) | (imagePixels[y][x] << 8) | (imagePixels[y][x]);
                image.setRGB(x, y, value);
            }
        }
        return image;
    }

    public static void writeImage(int[][] imagePixels, int width, int height, String outPath) {
        BufferedImage image = getBufferedImage(imagePixels, width, height);
        File ImageFile = new File(outPath);
        try {
            ImageIO.write(image, "jpg", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void decompress() throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream("compress");
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        int imgWidth = (int) input.readObject();
        int imgHeight = (int) input.readObject();
        int scaledWidth = (int) input.readObject();
        int scaledHeight = (int) input.readObject();
        int vectorWidth = (int) input.readObject();
        int vectorHeight = (int) input.readObject();
        Vector<Integer> VectorsToOptimizeIndices = (Vector<Integer>) input.readObject();
        Vector<Vector<Integer>> Quantized = (Vector<Vector<Integer>>) input.readObject();
        int[][] newImg = new int[scaledHeight][scaledWidth];
        for (int i = 0; i < VectorsToOptimizeIndices.size(); i++) {
            int x = i / (scaledWidth / vectorWidth);
            int y = i % (scaledWidth / vectorWidth);
            x *= vectorHeight;
            y *= vectorWidth;
            int v = 0;
            for (int j = x; j < x + vectorHeight; j++) {
                for (int k = y; k < y + vectorWidth; k++) {
                    newImg[j][k] = Quantized.get(VectorsToOptimizeIndices.get(i)).get(v++);
                }
            }
        }
        writeImage(newImg, imgWidth, imgHeight, "decompressed.jpg");
    }

    public static void main(String[] args) {
        try {
            compress(2, 2, 16,"image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            decompress();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
