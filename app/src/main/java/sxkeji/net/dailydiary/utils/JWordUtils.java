package sxkeji.net.dailydiary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.independentsoft.office.Unit;
import com.independentsoft.office.UnitType;
import com.independentsoft.office.drawing.Extents;
import com.independentsoft.office.drawing.Offset;
import com.independentsoft.office.drawing.Picture;
import com.independentsoft.office.drawing.PresetGeometry;
import com.independentsoft.office.drawing.ShapeType;
import com.independentsoft.office.drawing.Transform2D;
import com.independentsoft.office.word.DrawingObject;
import com.independentsoft.office.word.Paragraph;
import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.WordDocument;
import com.independentsoft.office.word.drawing.DrawingObjectSize;
import com.independentsoft.office.word.drawing.Inline;

import java.io.IOException;
import java.util.ArrayList;

import sxkeji.net.dailydiary.Article;

/**
 * word文档生成
 * Created by zhangshixin on 5/6/2016.
 */
public class JWordUtils {
    private static final String TAG = "JWordUtils";
    private Context mContext;
    private StringBuilder mSaveName;   //保存文件的名称，以时间保存
    private String mSavePath;   //保存文件地址
    private String mSaveType;   //保存文件类型
    private int mPictureWidth = 640;
    private int mPictureHeight = 480;

    public JWordUtils(Context mContext) {
        this.mContext = mContext;
        mSavePath = FileUtils.getExportDir(mContext);
        mSaveName = new StringBuilder(mSavePath);
        mSaveType = ".docx";
    }

    /**
     * 将article导出为文件
     *
     * @param articles
     */
    public void createArticle2Word(ArrayList<Article> articles) {
        if (articles != null) {
            for (Article article : articles) {
                createArticle2Word(article);
            }
        }
    }

    /**
     * 将article导出为文件
     *
     * @param article
     */
    public void createArticle2Word(Article article) {
        if (article != null) {
            WordDocument doc = new WordDocument();
            String date = article.getDate();
            String address = article.getAddress();
            String weather = article.getWeather();
            String title = article.getTitle();
            String content = article.getContent();
            String imgPath = article.getImg_path();

            mSaveName.append(content);
            mSaveName.append(mSaveType);
            addParagraph2Word(doc, date);
            addParagraph2Word(doc, address);
            addParagraph2Word(doc, weather);
//            addParagraph2Word(doc, title);
            addParagraph2Word(doc, content);
            addImg2Word(doc, imgPath);
            try {
                if (!TextUtils.isEmpty(mSaveName)) {
                    doc.save(String.valueOf(mSaveName), true);
                    Toast.makeText(mContext, "成功导出到 " + mSaveName, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "create doc success " + mSaveName);
                }
            } catch (IOException e) {
                Log.e(TAG, "create doc failed " + e.getMessage());
                Toast.makeText(mContext, "导出失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加照片
     * @param doc
     * @param imgPath
     */
    private void addImg2Word(WordDocument doc, String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            try {
                Picture picture = new Picture(imgPath);
                Unit width = new Unit(mPictureWidth, UnitType.PIXEL);
                Unit height = new Unit(mPictureHeight, UnitType.PIXEL);
                Offset offset = new Offset(0, 0);
                Extents extents = new Extents(width, height);

                picture.getShapeProperties().setPresetGeometry(new PresetGeometry(ShapeType.RECTANGLE));
                picture.getShapeProperties().setTransform2D(new Transform2D(offset, extents));
                picture.setID("1");
                picture.setName("image.jpg");

                Inline inline = new Inline(picture);
                inline.setSize(new DrawingObjectSize(width, height));
                inline.setID("1");
                inline.setName("Picture 1");
                inline.setDescription("image.jpg");

                DrawingObject drawingObject = new DrawingObject(inline);

                Run run1 = new Run();
                run1.add(drawingObject);

                Paragraph paragraph1 = new Paragraph();
                paragraph1.add(run1);

                doc.getBody().add(paragraph1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加段落
     *
     * @param doc
     * @param str
     */
    private static void addParagraph2Word(WordDocument doc, String str) {
        if (!TextUtils.isEmpty(str)) {
            Run run = new Run();
            run.addText(str);
            Paragraph paragraph = new Paragraph();
            paragraph.add(run);
            doc.getBody().add(paragraph);
        }
    }

    public static void create(String filePath) {
        WordDocument doc = new WordDocument();
        Run run = new Run();
        run.addText("Hello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsxHello world zsx");

        Paragraph paragraph = new Paragraph();
        paragraph.add(run);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(run);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(run);
        doc.getBody().add(paragraph);
        doc.getBody().add(paragraph2);
        doc.getBody().add(paragraph3);
        try {
            doc.save(filePath, true);
            Log.e(TAG, "create doc success " + filePath);
        } catch (IOException e) {
            Log.e(TAG, "create doc failed " + e.getMessage());
            e.printStackTrace();
        }
    }


}
