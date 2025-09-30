/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */





package org.dromara.surpass.web;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.dromara.surpass.authn.annotation.CurrentUser;
import org.dromara.surpass.crypto.Base64Utils;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.exception.BusinessException;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件上次实现/filestorage/upload/
 *
 * <p>上传文件到表MXK_FILE_STORAGE，当前无法存储大文件，需要第三方存储支持</p>
 *
 * @author Crystal.Sea
 *
 */
@Controller
@RequestMapping(value = { "/filestorage" })
public class FileStorageEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(FileStorageEndpoint.class);

	@Autowired
	FileStorageService fileUploadService;

 	@RequestMapping(value={"/upload/"})
 	@ResponseBody
 	public Message<Object> upload( HttpServletRequest request,
 	                            HttpServletResponse response,
 	                           @ModelAttribute FileStorage fileStorage,
 	                           @CurrentUser UserInfo currentUser){
 		logger.debug("FileUpload");
 		fileStorage.setId(WebContext.genId());
 		fileStorage.setContentType(fileStorage.getUploadFile().getContentType());
 		fileStorage.setFileName(fileStorage.getUploadFile().getOriginalFilename());
 		fileStorage.setContentSize(fileStorage.getUploadFile().getSize());
 		fileStorage.setCreatedBy(currentUser.getUsername());
 		/*
		 * upload UploadFile MultipartFile  to Uploaded Bytes
		 */
		if(null!=fileStorage.getUploadFile()&&!fileStorage.getUploadFile().isEmpty()){
			try {
				fileStorage.setDataStored(fileStorage.getUploadFile().getBytes());
				fileUploadService.save(fileStorage);
				logger.trace("FileUpload SUCCESS");
			} catch (IOException e) {
				logger.error("FileUpload IOException",e);
			}
		}
 		return new Message<>(Message.SUCCESS,(Object)fileStorage.getId());
 	}

 	@GetMapping(value={"/image/{id}"})
 	@ResponseBody
 	public Message<String> view(@PathVariable("id") String id){
 		FileStorage fileStorage = fileUploadService.getById(id);
 		if(fileStorage != null && fileStorage.getDataStored() != null) {
 			return new Message<>(Base64Utils.encodeImage(fileStorage.getDataStored()));
 		} else {
			 throw new BusinessException(400, "File Not Found");
		}
 	}

 	@GetMapping(value={"/image/{id}.png"})
 	public String viewPng(@PathVariable("id") String id){
 		FileStorage fileStorage = fileUploadService.getById(id);
 		if(fileStorage != null && fileStorage.getDataStored() != null) {
 			return Base64Utils.encodeImage(fileStorage.getDataStored());
 		}
 		return "";
 	}

 	@GetMapping(value={"/image/getByIds"})
 	@ResponseBody
 	public Message<List<FileStorage>> getByIds(@RequestParam("ids") List<String> ids){
 		List<FileStorage> fileStorageList = fileUploadService.listByIds(ids);
 		for(FileStorage fileStorage : fileStorageList) {
	 		if(fileStorage != null && fileStorage.getDataStored() != null) {
	 			fileStorage.setImageBase64(Base64Utils.encodeImage(fileStorage.getDataStored()));
	 		}
 		}
 		return new Message<>(Message.SUCCESS,fileStorageList);
 	}

 	@DeleteMapping(value={"/image/delete"})
 	@ResponseBody
 	public Message<String> delete(@RequestParam("ids") List<String> ids){
 		fileUploadService.removeBatchByIds(ids);
 		return new Message<>(Message.SUCCESS,"");
 	}


}
