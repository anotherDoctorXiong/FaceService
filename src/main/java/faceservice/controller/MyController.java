package faceservice.controller;

import faceservice.mapper.keyMapper;
import faceservice.service.FaceService;
import faceservice.service.HttpService;
import faceservice.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    private FaceService faceService;
    @Autowired
    private keyMapper keyMapper;
    @Autowired
    private ParkingService parkingService;



    @RequestMapping(value = "/TestGet", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getUrl(@RequestParam Map<String, Object> map) {
        map.put("code","0");
        map.put("message","success");
        map.put("timecost","1145");
        return new ResponseEntity(map,HttpStatus.OK);
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
            if (image==null) {

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
    /**
      * @name:          updateFace
      * @discraption:   通过人脸id更新图片
      * @param:         人脸id,人脸图片
      * @return:        成功返回200,失败返回失败信息
      **/
    @RequestMapping(value = "/Face/Update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateFace(@RequestParam Map<String, String> map, MultipartFile image) throws Exception {
        String id = map.get("id");
        if (id != null && id != ""&&!image.isEmpty()) {
            return faceService.updateFace(id,image);
        } else
            return new ResponseEntity("param not allowed null", HttpStatus.BAD_REQUEST);
    }
    /**
      * @name:          queryGroup
      * @discraption:   通过group查询该分组的所有人脸id
      * @param:         group<String></>
      * @return:        200返回List(id)
      **/
    @RequestMapping(value = "/Group/Query", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity queryGroup(@RequestBody Map<String, String> map){
        System.out.println(map.get("group"));
        String group=map.get("group");
        if (group != null && group != "") {
            List<String> list=faceService.queryGroup(group);
            if(list==null){
                return new ResponseEntity("can not find any id in this group", HttpStatus.BAD_REQUEST);
            }else
                return new ResponseEntity(list, HttpStatus.OK);
        } else
            return new ResponseEntity("param not allowed null", HttpStatus.BAD_REQUEST);

    }
    /**
     * @name:          deleteGroup
     * @discraption:   通过group删除该分组的所有人脸id
     * @param:         group<String></>
     * @return:        200删除成功
     **/
    @RequestMapping(value = "/Group/Delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> deleteGroup(@RequestBody Map<String, String> map){
        String group=map.get("group");
        if (group != null && group != "") {
            if(faceService.deleteGroup(group)==0){
                return new ResponseEntity("delete success group="+group, HttpStatus.OK);
            }else
                return new ResponseEntity("can not find any id in this group", HttpStatus.OK);
        } else
            return new ResponseEntity("param not allowed null", HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "Parking/Face/Add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> parkingFaceAdd(@RequestParam Map<String, String> map, MultipartFile image)throws IOException{
        String id=map.get("faceId");
        String name=map.get("username");
        if (id != null && id != ""&&name!=null&&name!=""&&image!=null) {
            map.put("wgId","");
            return new ResponseEntity(parkingService.parkingFaceAdd(map,image), HttpStatus.OK);
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
