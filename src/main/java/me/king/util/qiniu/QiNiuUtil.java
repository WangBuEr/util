package me.king.util.qiniu;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
@SuppressWarnings("unused")
public final class QiNiuUtil {
	private static final Logger LOG = LoggerFactory.getLogger(QiNiuUtil.class); 
	private String accessKey = null;
	private String secretKey = null;
	private Auth auth = null;
	private static final long expires = 10000;
	private static final String returnBody = "{\"key\": $(key), \"hash\": $(etag),\"mimeType\": $(mimeType)}";
	private static final String callbackBody = "{\"key\": $(key), \"hash\": $(etag),\"mimeType\": $(mimeType)}";
	private volatile static QiNiuUtil instance = null;
	private QiNiuUtil(){}
	
	private QiNiuUtil(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		auth = Auth.create(accessKey, secretKey);
	}

	public static QiNiuUtil getInstance() throws Exception{
		if(instance == null){
			synchronized (QiNiuUtil.class) {
				if(instance == null){
					Properties properties = loadProperties();
					String ak = properties.getProperty("accessKey");
					String sk = properties.getProperty("secretKey");
					if(StringUtils.isNullOrEmpty(ak) || StringUtils.isNullOrEmpty(sk)){
						throw new Exception("七牛配置信息错误");
					}else{
						instance = new QiNiuUtil(ak, sk);
					}
				}
			}
		}
		return instance;
	}
	public static QiNiuUtil getInstance(String accessKey,String secretKey){
		if(instance == null){
			synchronized (QiNiuUtil.class) {
				if(instance == null){
					Preconditions.checkNotNull(accessKey);
					Preconditions.checkNotNull(secretKey);
					instance = new QiNiuUtil(accessKey, secretKey);
				}
			}
		}
		return instance;
	}
	
	 public String getUpToken(String bucketName){
//		 auth.uploadToken(bucketName, null, 3600, policy)
		 return auth.uploadToken(bucketName);
	 }
	
	private static Properties loadProperties() throws Exception{
		String rootPath = QiNiuUtil.class.getClassLoader().getResource("").getPath();
		File propertiesFile = new File(rootPath + "/qiniu.properties");
		if(!propertiesFile.exists()){
			throw new Exception("qiniu.properties配置文件未找到");
		}
		Properties properties = new Properties();
		try(InputStream in = new FileInputStream(propertiesFile)){
			properties.load(in);
			return properties;
		}
	}
	
	public void deleteResources(String bucket,String key){
		BucketManager bm = new BucketManager(auth);
		try {
			bm.delete(bucket, key);
		} catch (QiniuException e) {
			LOG.error("删除文件{},失败：{}",key,e);
		}
	}
}
