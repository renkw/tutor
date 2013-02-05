/*
 * File   RandomCodeServlet.java
 * Create 2013/01/02
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * 随机生成验证码图片（gif格式）。
 * </p>
 * 
 * <p>
 * 设置参数：
 * <ul>
 * <li><strong>chars</strong> - 可选参数。全部包含的字符。默认为半角英数字。</li>
 * <li><strong>length</strong> - 可选参数。生成验证码长度。默认为4。</li>
 * <li><strong>font</strong> - 可选参数。生成验证码字体。默认为Arial-BOLD-20。</li>
 * </ul>
 * 
 * 生成图片宽高根据指定字体和随机字符串自动计算。
 * </p>
 * 
 * @author ren
 * 
 */
public class RandomCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 8117031543160270324L;

	private static final Logger logger = Logger
			.getLogger(RandomCodeServlet.class);

	static final Color[] BG = { new Color(0x0FFF0F5), new Color(0x0E6E6FA),
			new Color(0x0B0C4DE), new Color(0x0E0FFFF), new Color(0x0F0FFF0),
			new Color(0x0FAFAD2), new Color(0x0FFDAB9), new Color(0x0FFE4E1) };

	static final Color[] FG = { new Color(0x04B0082), new Color(0x0191970),
			new Color(0x02F4F4F), new Color(0x0556B2F), new Color(0x08B4513),
			new Color(0x0800000), new Color(0x0990000), new Color(0x0338800) };

	String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
	int length = 4;
	String font = "Arial-BOLD-20";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (logger.isTraceEnabled())
			logger.trace("[doGet] called");
		String code = RandomStringUtils.random(length, chars);
		if (logger.isDebugEnabled())
			logger.debug("[doGet] code = " + code);
		SessionContainer.get(req).setCheckCode(code);
		BufferedImage img = createImage(code);
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("image/gif");
		ImageIO.write(img, "gif", resp.getOutputStream());
	}

	/**
	 * <p>
	 * 根据设定生成图片。
	 * </p>
	 * 
	 * @param code
	 * @return
	 */
	protected BufferedImage createImage(String code) {
		Font font = Font.decode(this.font);
		// get font's width and height
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		FontMetrics fm = g.getFontMetrics(font);
		int base = fm.getAscent();
		int width = fm.stringWidth(code);
		int height = fm.getHeight();
		g.dispose();
		// create image
		img = new BufferedImage(width + 20, height, BufferedImage.TYPE_INT_RGB);
		g = img.getGraphics();
		g.setFont(font);
		// draw background
		g.setColor(BG[RandomUtils.nextInt(BG.length)]);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		interfer(g, img.getWidth(), img.getHeight());
		g.setColor(FG[RandomUtils.nextInt(FG.length)]);
		g.drawString(code, 10, base);
		g.dispose();
		return img;
	}

	/**
	 * <p>
	 * 在图片中添加干扰。
	 * </p>
	 * 
	 * @param g
	 */
	protected void interfer(Graphics g, int width, int height) {
		int n = RandomUtils.nextInt(3);
		for (int i = 0; i <= n; i++) {
			g.setColor(FG[RandomUtils.nextInt(FG.length)]);
			g.drawLine(RandomUtils.nextInt(width), RandomUtils.nextInt(height),
					RandomUtils.nextInt(width), RandomUtils.nextInt(height));
		}
	}

}
