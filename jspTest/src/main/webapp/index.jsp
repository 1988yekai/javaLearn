<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> 我的jsp页面 </title>
</head>
    <body>
        <h2>Hello World!</h2>
    <% out.println("Hello yek's jsp!"); %>
        <hr/>
    <%!
        private int initVar = 0;
        private int serviceVar = 0;
        private int destroyVar = 0;
    %>
    <%!
        public void jspInit() {
            initVar++;
            System.out.println("JspInit(): JSP被初始化了" + initVar + "次");
        }
        public void jspDestroy(){
            destroyVar++;
            System.out.println("JspDestroy(): JSP被摧毁了" + destroyVar + "次");
        }

    %>

    <%
        serviceVar++;
        System.out.println("_jspService(): JSP共响应了"+serviceVar+"次请求");

        String content1="初始化次数 : "+initVar;
        String content2="响应客户请求次数 : "+serviceVar;
        String content3="销毁次数 : "+destroyVar;
    %>
    <h1>JSP实例测试</h1>
    <p><%=content1%></p>
    <p><%=content2%></p>
    <p><%=content3%></p>
    </body>
</html>
