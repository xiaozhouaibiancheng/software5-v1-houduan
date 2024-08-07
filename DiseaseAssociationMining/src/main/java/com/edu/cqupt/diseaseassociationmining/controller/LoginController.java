package com.edu.cqupt.diseaseassociationmining.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.cqupt.diseaseassociationmining.common.MYRequest;
import com.edu.cqupt.diseaseassociationmining.common.Result;
import com.edu.cqupt.diseaseassociationmining.entity.User;
import com.edu.cqupt.diseaseassociationmining.service.UserService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@RestController
public class LoginController {

    @Resource
    private UserService usersercive;


    /**
     *  测试单点登录   对接绵阳三医院
     */

    @GetMapping("/login")
    public Result login(@RequestParam String repKey) throws URISyntaxException, IOException {

        System.out.println("repKey 是  ======= 》 " + repKey);

        // 请求地址 拿到key
        String url1 = "http://192.168.1.39/API/BIAServicesToSSO.asmx/QueryKey?";

        MYRequest request=new MYRequest();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String now = simpleDateFormat.format(date);

        request.setReqTime(now);
        request.setSerialNo("02");
        request.setReqKey(repKey);

        String json = JSON.toJSONString(request);
        // 对 JSON 字符串进行 URL 编码
        String encodedReqStr = URLEncoder.encode(json, "UTF-8");

        String url2  = url1 + "reqStr=" + encodedReqStr;

        URL url = new URL(url2);

        // 创建连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为GET
        connection.setRequestMethod("GET");

        // 获取响应代码
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        // 读取响应内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // 输出响应内容
        String toString = response.toString();
        System.out.println(toString);

        // 关闭连接
        connection.disconnect();
        String content = null;

        try {
            // 创建一个DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 将XML字符串解析为Document对象
            Document doc = builder.parse(new org.xml.sax.InputSource(new java.io.StringReader(toString)));

            // 获取所有名为 "string" 的元素
            NodeList stringNodes = doc.getElementsByTagName("string");

            // 提取 <string> 元素中的内容
            if (stringNodes.getLength() > 0) {
                Element stringElement = (Element) stringNodes.item(0);
                content = stringElement.getTextContent();
                System.out.println("Extracted Code: " + content);
            } else {
                System.out.println("No <string> elements found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 拿到代码
        String decryptedString = null;
        System.out.println("daima  ====" +content);

        try {
            Security.addProvider(new BouncyCastleProvider());

            //String encryptedString = "zlWJJLU9cDcDNufBWCXDm5vt1RFz+FN9AGsJSWOyBkiR/f5WHlQJip82iUCJDzpMg6lwcadIzqakbw96STC8Sac1CBtegU1ITtlXSvpbkPxVkZ8pXWblg+TofVrsu8MQsKk5hf7HC780j+I0WDpKvI0hEbOwd4dmf7EMyx5nOeAh75usWvVfHvwgOpp2I6i5bcWUYwsHi6fhWnbVSLMcueyIxNtq+vYMNQ6l2b98QT70yVH75OCamuCLcsHXrtq+wozqnerQETPuw92Zdl6KfA==";

            // 密钥，需要与加密时使用的密钥一致
            String key = "UISwD9fW6cFh9SNS"; // 这里需要替换为加密时使用的密钥

            // 使用Base64解码密文
            byte[] decodedBytes = Base64.getDecoder().decode(content);

            // 创建SM4解密实例
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "SM4"));

            // 解密
            byte[] decrypted = cipher.doFinal(decodedBytes);

            // 将解密后的数据转换成字符串
            decryptedString= new String(decrypted);
            System.out.println("Decrypted String: " + decryptedString);


        } catch (Exception e) {
            e.printStackTrace();
        }



        //  string 转json
        JSONObject jsonObject = JSONObject.parseObject(decryptedString);

        String resultCode = jsonObject.getString("resultCode");
        //这里有问题，应该是username。
        String UserName = jsonObject.getJSONObject("respBody").getString("UserCode");
        User user = usersercive.getUserByUserName(UserName);
        System.out.println("解密后的json" + resultCode);
        System.out.println("解密后的用户名" + UserName);


        // TODO   根据code来调整业务
        if (!resultCode.equals("-1")){
            return  Result.success(200,"对接系统成功",user);
        }
        return  Result.fail(500,"对接失败",jsonObject) ;
    }





}
