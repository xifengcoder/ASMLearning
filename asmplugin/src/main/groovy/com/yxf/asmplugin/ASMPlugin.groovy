package com.yxf.asmplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ASMPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("------------------ASMPlugin插件 开始----------------------");
        def android = project.extensions.getByType(AppExtension)

        //注册一个Transform
        def classTransform = new MyClassTransform(project);
        android.registerTransform(classTransform);

        //创建一个Extension，名字叫做testCreateJavaConfig 里面可配置的属性参照MyPluginTestClass
        project.extensions.create("testCreateJavaConfig", MyPluginTestClass)

        if (project.plugins.hasPlugin(AppPlugin)) {
            android.applicationVariants.all { variant ->
                def variantData = variant.variantData
                def scope = variantData.scope

                def configExtension = project.extensions.getByName("testCreateJavaConfig");

                //创建一个task
                def createTaskName = scope.getTaskName("CeShi", "MyTestPlugin") //CeShi[Debug/Release]MyTestPlugin
                def createTask = project.task(createTaskName)

                createTask.doLast {
                    createJavaTest(variant, configExtension)
                }

                String generateBuildConfigTaskName = variant.getGenerateBuildConfigProvider().name //generate[Debug/Release]BuildConfig

                //执行generate[Debug/Release]BuildConfig时, 后面会跟着执行CeShi[Debug/Release]MyTestPlugin;
                //执行CeShi[Debug/Release]MyTestPlugin时, 需要依赖generate[Debug/Release]BuildConfig的完成.
                def generateBuildConfigTask = project.tasks.getByName(generateBuildConfigTaskName)
                if (generateBuildConfigTask) {
                    createTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy createTask
                }
            }

        }
        System.out.println("------------------结束了吗----------------------");
    }

    static void createJavaTest(variant, config) {
        println "createJavaTest config.str = " + config.str
        //要生成的内容
        def content = """package com.yxf.asm;

public class MyPluginTest {
    public static final String str = "${config.str}";
}
""";
        //获取到BuildConfig类的路径: app/build/generated/source/buildConfig/debug
        File outputDir = variant.getVariantData().getScope().getBuildConfigSourceOutputDir()

        def javaFile = new File(outputDir, "MyPluginTest.java")

        javaFile.write(content, 'UTF-8')
    }
}

class MyPluginTestClass {
    def str = "默认值";
}