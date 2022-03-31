package com.mars.java.classload;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
@SupportedSourceVersion(SourceVersion.RELEASE_6)// 只支持JDK 6的Java代码
@SupportedAnnotationTypes("*")// 可以用"*"表示支持所有Annotations
public class NameCheckProcessor extends AbstractProcessor {
    private NameChecker nameChecker;
    /**
     * 初始化名称检查插件 */
    @Override
    public void init(ProcessingEnvironment processingEnv) { super.init(processingEnv);
        nameChecker = new NameChecker(processingEnv);
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements())
                nameChecker.checkNames(element);
        }
        return false;
    }
}
