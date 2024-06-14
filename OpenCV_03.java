import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OpenCV_03 {
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	static String fileName;
	static Mat inImage, outImage;
	static int inH, inW, outH, outW;
	static JFrame frame;
	static Container contentPane;
	static MyPanel panel;
	static JMenuBar menuBar;
	
	static class MyPanel extends JPanel {
		public void paintComponent(Graphics g) { 
			super.paintComponent(g);
			double RGB[];
			int R, G, B;
			for (int i=0; i < outH; i++) {
				for (int k=0; k < outW; k++) {
					RGB = outImage.get(k,i);
					if (outImage.channels() == 3) { // 칼라 영상
						B = (int)RGB[0];
						G = (int)RGB[1];
						R = (int)RGB[2];
					} else { // 그레이스케일 또는 흑백 영상
						R = G = B = (int)RGB[0];
					}
					g.setColor(new Color(R, G, B));
					g.drawRect(i, k, 1, 1);
				}
			}
			
		}
	}
	
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("openCV 연습");
		frame.setLayout(new CardLayout());	
		
		panel = new MyPanel();
		frame.add(panel, BorderLayout.CENTER);		
		contentPane = frame.getContentPane();	
					
		addMenu();		
		
		frame.setSize(400, 500); //초기 윈도창 크기
		frame.setResizable(false);
		frame.setVisible(true);        
	}
	
	static void display() {
		Graphics g = contentPane.getGraphics();
		contentPane.paintAll(g); 
		frame.setPreferredSize(new Dimension(outH+15, outW+63));
		frame.pack();
	}
	
	static void addMenu() {
		Font f = new Font("맑은고딕", Font.PLAIN, 12);
		UIManager.put("Menu.font", f);
		UIManager.put("MenuItem.font", f);
		
		menuBar = new JMenuBar();	
		frame.setJMenuBar(menuBar);
		
		// 파일 메뉴
		JMenu fileMenu = new JMenu("파일");
		JMenuItem openItem = new JMenuItem("이미지 열기");
		JMenuItem saveItem = new JMenuItem("이미지 저장");
		JMenuItem closeItem = new JMenuItem("프로그램 종료");
				
		menuBar.add(fileMenu);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(closeItem);
		
		// 사진 효과(1) 메뉴
		JMenu Photo1Menu = new JMenu("사진 효과(1)");
		JMenuItem zoomItem = new JMenuItem("확대/축소");
		JMenuItem reverseUpItem = new JMenuItem("상하 반전");
		JMenuItem reverseLeftItem = new JMenuItem("좌우 반전");
		JMenuItem rotate90Item = new JMenuItem("90도 회전");
		JMenuItem rotate180Item = new JMenuItem("180도 회전");
		JMenuItem rotate270Item = new JMenuItem("270도 회전");
		JMenuItem rotateFreeItem = new JMenuItem("자유 회전");
		
		menuBar.add(Photo1Menu);
		Photo1Menu.add(zoomItem);
		Photo1Menu.add(reverseUpItem);
		Photo1Menu.add(reverseLeftItem);
		Photo1Menu.add(rotate90Item);
		Photo1Menu.add(rotate180Item);
		Photo1Menu.add(rotate270Item);
		Photo1Menu.add(rotateFreeItem);
		
		// 사진 효과(2) 메뉴
		JMenu Photo2Menu = new JMenu("사진 효과(2)");
		JMenuItem addItem = new JMenuItem("밝게/어둡게");
		JMenuItem grayscaleItem = new JMenuItem("회색 영상");
		JMenuItem blackWhiteItem = new JMenuItem("흑백 영상");
		JMenuItem blurItem = new JMenuItem("블러링");
		JMenuItem cannyItem = new JMenuItem("경계선 추출");
		JMenuItem equalItem = new JMenuItem("평활화");
		
		menuBar.add(Photo2Menu);
		Photo2Menu.add(addItem);
		Photo2Menu.add(grayscaleItem);
		Photo2Menu.add(blackWhiteItem);
		Photo2Menu.add(blurItem);
		Photo2Menu.add(cannyItem);
		Photo2Menu.add(equalItem);	
		
		// 메뉴 선택시 실행하는 메서드
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame, "파일 열기", FileDialog.LOAD);
				fd.setVisible(true);
				fileName = fd.getDirectory() + fd.getFile();
				inImage = Imgcodecs.imread(fileName); 
				inH = inImage.cols();
				inW = inImage.rows(); 
                
				outImage = inImage.clone();
				outH = outImage.cols();
				outW = outImage.rows();

				display();
			}			
		});
		
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame, "파일 저장", FileDialog.SAVE);
				fd.setVisible(true);
				String saveName = fd.getDirectory() + fd.getFile();
				Imgcodecs.imwrite(saveName,outImage);
				JOptionPane.showMessageDialog(frame, saveName + "으로 저장됨", "성공", JOptionPane.INFORMATION_MESSAGE);
			}			
		});
		
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		
		zoomItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double scale = Double.parseDouble(JOptionPane.showInputDialog(frame, "확대/축소 배율", 2));
				outH = (int)(inImage.cols()*scale);
				outW = (int)(inImage.rows()*scale);
				Size sz = new Size(outH, outW);
				Imgproc.resize(inImage, outImage, sz);				
				display();				
			}			
		});
		
		reverseUpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;
				Core.flip(inImage, outImage, 0);  
				display();	
			}			
		});
		
		reverseLeftItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;
				Core.flip(inImage, outImage, 1);  
				display();	
			}			
		});
		
		rotate90Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inW; // 높이와 폭이 바뀜
				outW = inH;
				Core.rotate (inImage, outImage, Core.ROTATE_90_CLOCKWISE); 
				display();	
			}			
		});
		
		rotate180Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;
				Core.rotate (inImage, outImage, Core.ROTATE_180); 
				display();
			}			
		});
		
		rotate270Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inW; // 높이와 폭이 바뀜
				outW = inH;
				Core.rotate (inImage, outImage, Core.ROTATE_90_COUNTERCLOCKWISE); 
				display();
			}			
		});
		
		rotateFreeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				int angle = Integer.parseInt(JOptionPane.showInputDialog(frame, "회전 각도", 45));
				Point center=new Point(inImage.cols()/2,inImage.rows()/2); 
				Mat rotMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);
				Imgproc.warpAffine(inImage, outImage, rotMat, inImage.size());
				display();
			}			
		});
		
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				int value = Integer.parseInt(JOptionPane.showInputDialog(frame, "밝게/어둡게", -100));
				inImage.convertTo(outImage, -1, 1, value); 
				display();
			}			
		});
		
		grayscaleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				Imgproc.cvtColor(inImage, outImage, Imgproc.COLOR_RGB2GRAY);
				display();
			}			
		});
		
		blackWhiteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				Imgproc.cvtColor(inImage, outImage, Imgproc.COLOR_RGB2GRAY);
				Imgproc.threshold(outImage, outImage , 127 , 255, Imgproc.THRESH_BINARY);
				display();
			}			
		});
		
		blurItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				int value = Integer.parseInt(JOptionPane.showInputDialog(frame, "블러링", 9));
				Imgproc.blur(inImage, outImage, new Size(value, value));
				display();
			}			
		});
		
		cannyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				int value = Integer.parseInt(JOptionPane.showInputDialog(frame, "곱하기 값", 3));
				Imgproc.Canny(inImage, outImage, 100, 100*value);
				display();
			}			
		});
		
		equalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outH = inH;
				outW = inW;	
				Imgproc.cvtColor(inImage, outImage, Imgproc.COLOR_RGB2GRAY);
				Imgproc.equalizeHist(outImage, outImage);
				display();
			}			
		});
	}
}