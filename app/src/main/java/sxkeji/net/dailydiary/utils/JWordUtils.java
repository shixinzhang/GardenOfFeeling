package sxkeji.net.dailydiary.utils;

import android.util.Log;

import com.independentsoft.office.word.Paragraph;
import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.WordDocument;

import java.io.IOException;

/**
 * word文档生成
 * Created by zhangshixin on 5/6/2016.
 */
public class JWordUtils {
    private static final String TAG = "JWordUtils";

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
