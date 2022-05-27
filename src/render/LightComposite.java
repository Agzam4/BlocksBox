package render;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class LightComposite implements Composite {

    public static LightComposite INSTANCE = new LightComposite();

    private LightContext context = new LightContext();

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return context;
    }
    
    
    private static class LightContext implements CompositeContext {

        @Override
        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {

        	int width = Math.min(src.getWidth(), dstIn.getWidth());
        	int height = Math.min(src.getHeight(), dstIn.getHeight());

        	float alpha = 1;

        	int[] result = new int[4];
        	int[] srcPixel = new int[4];
        	int[] dstPixel = new int[4];
        	int[] srcPixels = new int[width];
        	int[] dstPixels = new int[width];

        	for (int y = 0; y < height; y++) {
        		src.getDataElements(0, y, width, 1, srcPixels);
        		dstIn.getDataElements(0, y, width, 1, dstPixels);
        		for (int x = 0; x < width; x++) {
        			int pixel = srcPixels[x];
        			srcPixel[0] = (pixel >> 16) & 0xFF;
        			srcPixel[1] = (pixel >>  8) & 0xFF;
        			srcPixel[2] = (pixel      ) & 0xFF;
        			srcPixel[3] = (pixel >> 24) & 0xFF;

        			pixel = dstPixels[x];
        			dstPixel[0] = (pixel >> 16) & 0xFF;
        			dstPixel[1] = (pixel >>  8) & 0xFF;
        			dstPixel[2] = (pixel      ) & 0xFF;
        			dstPixel[3] = (pixel >> 24) & 0xFF;

        			blend(srcPixel, dstPixel, result);

        			dstPixels[x] = ((int) (dstPixel[3] + (result[3] - dstPixel[3]) * alpha) & 0xFF) << 24 |
        					((int) (dstPixel[0] + (result[0] - dstPixel[0]) * alpha) & 0xFF) << 16 |
        					((int) (dstPixel[1] + (result[1] - dstPixel[1]) * alpha) & 0xFF) <<  8 |
        					(int) (dstPixel[2] + (result[2] - dstPixel[2]) * alpha) & 0xFF;
        		}
        		dstOut.setDataElements(0, y, width, 1, dstPixels);
        	}
        }
        
        public void blend(int[] src, int[] dst, int[] result) {
            result[0] = Math.min(255, src[0] + dst[0]);
            result[1] = Math.min(255, src[1] + dst[1]);
            result[2] = Math.min(255, src[2] + dst[2]);
            result[3] = Math.min(255, src[3] + dst[3]);
        }

        @Override
        public void dispose() {
        }
    }
}