package com.command.transfer.controller;

import com.command.transfer.chord.com.chord.data.Node;
import com.command.transfer.common.ErrorCode;
import com.command.transfer.common.Response;
import com.command.transfer.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

/**
 * FileController
 *
 * @author liulei
 * @date 2020/11/6
 */
@Controller
@CrossOrigin
public class FileController {

    public static final String PICTUREURL = "D:/nginx-1.13.8/html/slitLamp/screening/";

    @Resource
    FileService fileService;

    /** 
     * 描述: 创建节点
     * 
     * @Author liulei
     * @Date 2020/11/12 上午10:20
     * @Param 
     * @return 
    */
    @ResponseBody
    @GetMapping("/node/create")
    public Response createNode() {
        try {
            //获取主机Ip地址
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = localHost.getHostAddress();
            
            //创建节点
            Node.CreateNewNode(ip, 9527, "");
        } catch (UnknownHostException | SocketException e) {
            return Response.error(ErrorCode.PARSE_FAIL, null);
        }
        return Response.success(null);
    }


    /** 
     * 描述: 上传文件
     * 
     * @Author liulei
     * @Date 2020/11/12 上午10:21
     * @Param 
     * @return 
    */ 
    @PostMapping("/transfer/upload")
    @ResponseBody
    public Response upload(@RequestParam(value = "files", required = false) MultipartFile files) throws IOException, NoSuchMethodException {
        return fileService.upload(files);
    }


    @GetMapping("/transfer/downloadFile")
    public void download(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        fileService.download(fileName, request, response);
    }

    @GetMapping("/remote/downloadFile")
    public void remoteDownload(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取文件的绝对路径
        //String realPath = request.getSession().getServletContext().getRealPath("upload");
        //获取输入流对象（用于读文件）
        File file = new File("./Users/itinypocket/workspace/temp", fileName);
        final boolean exists = file.exists();
        FileInputStream fis = new FileInputStream(file);
        //获取文件后缀（.txt）
        String extendFileName = fileName.substring(fileName.lastIndexOf('.'));

        response.setContentType("application/octet-stream");//
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,"UTF-8"));// 设置文件名

        //动态设置响应类型，根据前台传递文件类型设置响应类型
        response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
        //获取输出流对象（用于写文件）
        ServletOutputStream os = response.getOutputStream();
        //下载文件,使用spring框架中的FileCopyUtils工具
        FileCopyUtils.copy(fis,os);
        os.flush();
    }
}
