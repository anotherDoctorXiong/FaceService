package faceservice.controller;

import faceservice.mapper.keyMapper;
import faceservice.service.FaceService;
import faceservice.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Controller
public class MyController {
    @Autowired
    private HttpService httpService;
    @Autowired
    private FaceService faceService;
    @Autowired
    private keyMapper keyMapper;

    @RequestMapping(value = "/TestGet", method = RequestMethod.GET)
    @ResponseBody
    public void getUrl(@RequestParam Map<String, Object> map) {
        httpService.get("api/version/v1", map);
    }

    /**
     * @name: addFace
     * @discraption: 接收http post请求 参数放form里面
     * @param: type<String>(1是使用facepass)</>id<String>(人脸id),group<String>(分组id),image<MultipartFile>(人脸图片)
     * @return: 添加成功返回200, 失败返回400包含错误信息
     **/
    @RequestMapping(value = "/Face/Add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addFace(@RequestParam Map<String, String> map, MultipartFile image) throws Exception {
        String id = map.get("id");
        String type = map.get("type");
        String group = map.get("group");
        if (id != null && id != "" && type != null && type != "" && group != null && group != "") {
            if (image.isEmpty()) {
                return new ResponseEntity("no image was found", HttpStatus.BAD_REQUEST);
            }
            return faceService.addFace(type, id, group, image);
        } else {
            return new ResponseEntity("params not allowed null", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @name: deleteFace
     * @discraption: 接收http  json数据
     * @param: id<String>(人脸id)
     * @return: 删除成功返回200  失败返回失败信息
     **/
    @RequestMapping(value = "/Face/Delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteFace(@RequestBody Map<String, String> map) throws InterruptedException {
        String id = map.get("id");
        if (id != null && id != "") {
            return faceService.deleteFace(id);
        } else
            return new ResponseEntity("param not allowed null", HttpStatus.BAD_REQUEST);
    }
    /**
      * @name:          queryFace
      * @discraption:   通过人脸id查询该人脸是否存在
      * @param:         人脸id
      * @return:        返回200该人脸已被添加，失败返回错误信息
      **/
    @RequestMapping(value = "/Face/Query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> queryFace(@RequestBody Map<String, String> map) throws InterruptedException {
        String id = map.get("id");
        if (id != null && id != "") {
            return faceService.queryFace(id);
        } else
            return new ResponseEntity("param not allowed null", HttpStatus.BAD_REQUEST);
    }

    /*
   * post json使用实例
   * */
    @RequestMapping(value = "/Test", method = RequestMethod.GET)
    @ResponseBody
    public void Test(@RequestParam Map<String, String> map) {
        System.out.println(keyMapper.getAll().size());
        System.out.println(keyMapper.getOne("sdisfasf==").toString());
    }
}
