# Rigger
是一个处理权限请求和onActivityResult的工具类,不再需要onRequestPermissionsResult和onActivityResult中处理逻辑

## requestPermissions
```
Rigger.on(this@MainActivity)
       .requestCode(REQUEST_CODE)
       .permissions(arrayOf(WRITE_EXTERNAL_STORAGE, CALL_PHONE))
       .request(object : PermissionCallback() {
              override fun onGranted(permission: String) {
                    Toast.makeText(this@MainActivity,"请求"+permission+"成功",Toast.LENGTH_LONG).show()
              }
              
              override fun onDenied(permission: String) {
                      super.onDenied(permission)
                      Toast.makeText(this@MainActivity,permission+"取消",Toast.LENGTH_LONG).show()
              }
            })
```

## startActivityForResult
```
   Rigger.on(this@MainActivity)
          .requestCode(REQUEST_CODE)
          .targetActivity(TestActivity::class.java)
          .put("key","main")
          .start(object : ActivityResultCallback() {
                  override fun onResult(data: Intent?) {
                          super.onResult(data)
                          Toast.makeText(this@MainActivity,"result:"+data?.getStringExtra("key"),
                                    Toast.LENGTH_LONG).show()
                  }
             })
```

## 更新日志

##### Rigger 1.0
> * 封装便捷的权限请求和onActivityResult
> * 一些常用的系统app工具类,在一些需要权限的app,如:相机、拨号,内部增加权限判断,不需要外部处理

##### Rigger 1.1
尚未开发。预计开发目标,会增加intent取值的注解.

## 提醒
Rigger未提交到maven库,需要使用,自行下载工程，在git bash,输入以下命令:
```
git clone git@github.com:xiexiang89/rigger.git
```
