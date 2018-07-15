# rigger
Android 权限请求 activityresult工具类,支持在回调方法中处理界面回调结果

### requestPermissions
```
            Rigger.on(this@MainActivity)
                    .requestCode(REQUEST_CODE)
                    .permissions(arrayOf(WRITE_EXTERNAL_STORAGE, CALL_PHONE))
                    .request(object : PermissionCallback() {
                        override fun onRequestPermissionSuccess(permission: String) {
                            Toast.makeText(this@MainActivity,"请求"+permission+"成功",Toast.LENGTH_LONG).show()
                        }

                        override fun onDenied(permission: String) {
                            super.onDenied(permission)
                            Toast.makeText(this@MainActivity,permission+"取消",Toast.LENGTH_LONG).show()
                        }
                    })
```
