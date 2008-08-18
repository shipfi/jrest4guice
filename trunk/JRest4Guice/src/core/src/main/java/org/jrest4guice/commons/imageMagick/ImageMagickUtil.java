package org.jrest4guice.commons.imageMagick;

import org.jrest4guice.commons.lang.RuntimeExector;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>

	composite -dissolve 100% -gravity SouthEast logoWatermark.png -resize 300% girl.jpg girl_watermark.jpg
	-gravity type
		direction primitive gravitates to when annotating the image.
		Choices are: NorthWest, North, NorthEast, West, Center, East, SouthWest, South, SouthEast. Or you can use -list with a 'Gravity' option to get a complete list of -
		gravity settings available in your ImageMagick installation.
 *
 *
 */
public class ImageMagickUtil {
	
	/**
	 * 制作缩略图
	 * @param source	原始文件地址
	 * @param target	目标文件地址
	 * @param width		输出的宽度
	 * @param height	输出的高度
	 * @throws Exception
	 */
	public void makeThumbnail(String source, String target, int width,
			int height) throws Exception {
		RuntimeExector.execute("convert "+source+" -auto-orient -thumbnail "+width+"x"+height+" "+target);
	}
	
	/**
	 * 制作缩略图
	 * @param source	原始文件地址
	 * @param target	目标文件地址
	 * @param width		输出的宽度的百分比
	 * @param height	输出的高度的百分比
	 * @throws Exception
	 */
	public void makeThumbnail4Percent(String source, String target, int width,
			int height) throws Exception {
		RuntimeExector.execute("convert "+source+" -auto-orient -thumbnail "+width+"%x"+height+"% "+target);
	}

	/**
	 * 给图片打上文字水印
	 * @param source
	 * @param watermark
	 * @param color
	 * @param pointsize
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public void makeTextWaterark(
			String source, 
			String watermark,
			String color,
			int pointsize,
			int x,
			int y) throws Exception {
		RuntimeExector.execute("convert -fill "+color+" -pointsize "+pointsize+" -draw 'text "+x+","+y+" \""+watermark+"\"' "+source);
	}

	/**
	 * 给图片打上图片水印
	 * @param source	原始图片
	 * @param target	输出图片
	 * @param watermarkImg	水印图片
	 * @param watermarkResize	水印图片的缩放比例
	 * @param dirction	水印的方位
	 * @throws Exception
	 */
	public void makeImageWaterark(
			String source, 
			String target, 
			String watermarkImg,
			String watermarkResize,
			Direction dirction) throws Exception {
		RuntimeExector.execute("composite -dissolve 100% -gravity "+dirction.toString()+" "+watermarkImg+" -resize "+watermarkResize+" "+source+" "+target);
	}

	public static void main(String[] args) {
		try {
//			RuntimeExector.execute("ping 192.168.16.36 -t");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
