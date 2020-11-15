package com.command.transfer.service.impl;

import com.command.transfer.chord.com.chord.data.JanTing;
import com.command.transfer.chord.com.chord.data.Node;
import com.command.transfer.chord.com.chord.tool.SHA1;
import com.command.transfer.common.ErrorCode;
import com.command.transfer.common.Response;
import com.command.transfer.service.FileService;
import okhttp3.*;
import okio.BufferedSource;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.Map;

/**
 * ServiceImpl
 *
 * @author liulei
 * @date 2020/11/9
 */
@Service
public class FileServiceImpl implements FileService {

    public static final String CONFIGUREURL = "http://10.11.23.203:8029/";

    OkHttpClient client = new OkHttpClient();

    JanTing janTing = new JanTing();

    @Override
    public Response getFile() {
        return Response.success(null);
    }

    @Override
    public Response upload(MultipartFile file) throws IOException, NoSuchMethodException {
        String fileName = file.getOriginalFilename();
        long sha1 = SHA1.GetHash(fileName);
        Method qwe = JanTing.class.getDeclaredMethod("qwe",String.class);

        Node.Instance().FindFile(sha1, fileName, qwe, janTing);

        String filePath = "./Users/itinypocket/workspace/temp";
        File path = new File(filePath); //判断文件路径下的文件夹是否存在，不存在则创建
        if (!path.exists()) {
            boolean mkdirs = path.mkdirs();
            System.out.println(mkdirs);
        }
        File dest = new File(filePath, fileName);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),dest);
        } catch (IOException e) {
            return Response.error(ErrorCode.PARSE_FAIL, null);
        }
        return Response.success(null);
    }

    @Override
    public void download(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        long l = SHA1.GetHash(fileName);

        String url = "http://localhost:9527/remote/downloadFile?fileName=" + fileName;
        final Request request1 = new Request.Builder()
                .url(url)
                .get()
                .build();
        final okhttp3.Response response1 = client.newCall(request1).execute();
        if (response1.isSuccessful()) {
            ResponseBody body = response1.body();
            long contentLength = body.contentLength();
            BufferedSource source = body.source();

            response.setContentType("application/octet-stream");//
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"UTF-8"));// 设置文件名

            //获取文件后缀（.txt）
            String extendFileName = fileName.substring(fileName.lastIndexOf('.'));
            //动态设置响应类型，根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //获取输出流对象（用于写文件）
            ServletOutputStream os = response.getOutputStream();
            //下载文件,使用spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(source.inputStream(),os);
            os.flush();
        }
    }
}
