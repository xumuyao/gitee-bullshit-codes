package com.pdfengine.pspdfdata.svc;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.pdfengine.pspdfdata.util.DataUtil;

public class JavaSwitch {
    public static void main(String[] args) {
        Element studentsEle = DocumentHelper.createDocument().addElement("Students");
        String[] rowNames = new String[] {"xiaoming", "xiaohong", "xiaohua"};
        String[] colNames = new String[] {"Chinese", "English", "Math"};
        for (int i = 0; i < rowNames.length; i++) {
            Element studentEle = studentsEle.addElement("Student");
            for (int j = 0; j < colNames.length; j++) {
                switch (j) {
                    case 0:
                        studentEle.addAttribute(colNames[j], DataUtil.getString(rowNames[i]));
                        break;
                    case 1:
                        studentEle.addAttribute(colNames[j], DataUtil.getString(rowNames[i]));
                        break;
                    case 2:
                        studentEle.addAttribute(colNames[j], DataUtil.getString(rowNames[i]));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
