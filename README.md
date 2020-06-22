# 移动应用开发大作业文档
## 小组成员
+ 杨明举：负责前端主界面、主界面点击之后的详情界面、搜索界面、关注列表和聊天功能 
+ 申腾：负责前端登录、注册、登出、个人中心、修改个人信息、添加兴趣标签和修改密码以及文档的撰写 
+ 亓泽鹏：负责后端服务器部署和数据库管理以及提供前端所需要的所有接口
## Github仓库地址
<https://github.com/Tdout/AndroidAPP>
## 功能完成情况
+ 已完成：登录、注册、登出、修改个人信息、添加兴趣标签、修改密码、查看关注列表、聊天、浏览信息、本地选取图片并上传、搜索
+ 功能亮点：<u> 圆形头像、轮播页、添加标签并可以按照相应标签进行搜索、本地选取照片之后可以进行裁剪</u>
+ 待优化：用户信息存储、聊天等刷新机制
+ 未完成：~~身份认证~~、~~找回密码~~、~~聊天发送图片~~、~~拍照并上传图片~~、~~下拉刷新~~、~~发布信息~~
## 前端文档
### 前端界面
详细样式请看视频展示
### 用户信息存储
将用户登录时的信息保存成了全局变量进行简单的存储，未使用sharedPreference进行本地的存储
### 前后端数据交互
新开一个线程请求网络,将所需传给后端的请求保存为<kbd>JSON</kbd>格式，采用<kbd>"POST"</kbd>方式请求后端，获取后端的输出流并存为字符串并进行JSON解析或其他处理。
``` java
Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = null;
                            JSONObject userJSON = new JSONObject();
                            userJSON.put("user_id", userName);
                            userJSON.put("password", psw);
                            String content = String.valueOf(userJSON);
                            HttpURLConnection connection = (HttpURLConnection) new URL(LoginAddress).openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Charset", "UTF-8");
                            connection.setRequestProperty("accept", "application/json");
                            if (content != null && !TextUtils.isEmpty(content)) {
                                byte[] writebytes = content.getBytes();
                                connection.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                                OutputStream os = connection.getOutputStream();
                                os.write(content.getBytes());
                                os.flush();
                                os.close();
                            }
                            if (connection.getResponseCode() == 200) {
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                result = reader.readLine();
                            }
                        } catch (Exception e) {
                            System.out.println("failed");
                        }
                    }
                });
```
### 前端的图片的获取
采用第三方库<kbd>Glide</kbd>,从URL静态获取相应图片并使其显示为圆形图片：
```java
Glide.with(this).load(MainActivity.global_url+"/static/"+MainActivity.global_login_id+"/img.jpg").
                apply(new RequestOptions().
                        placeholder(R.drawable.login).
                        error(R.drawable.login))
                .apply(new RequestOptions().transform(new CircleCrop()))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(new RequestOptions().skipMemoryCache(true))
                .into(mHHead);
```
### 聊天功能的实现
首先先同步获取历史消息，随后设置handler对异步线程进行监听。
重写线程，设置为一秒一次刷新，然后将提醒(msg.what)传到到handler里。
```java
//开一个线程继承Thread
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();
            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000);
                    Message msg = new Message();
                    //消息内容 为MSG_ONE
                    msg.what = 1;
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(msgAct.isFinishing()){
                    break;
                }
            } while (true);
        }
    }
```
由handler开线程从后端获取信息进行聊天数据刷新:
```java
private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message hmsg){
            super.handleMessage(hmsg);
            switch (hmsg.what){
                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String jsonData = get_msg_from_DB(TargetID);
                            try {
                                //JSONObject result_json=new JSONObject(jsonData);
                                JSONArray user_list=new JSONArray(jsonData);
                                // 数据分配
                                msgList.clear();
                                for (int i = 0;i<user_list.length();i++){
                                    JSONObject object=user_list.getJSONObject(i);
                                    System.out.println(object);
                                    String content = object.getString("content");
                                    if(object.getString("sender").equals(MainActivity.global_login_id)){
                                        // 发送者为本人
                                        Msg msg = new Msg(content, Msg.TYPE_SEND);
                                        msgList.add(msg);
                                    }
                                    else if(object.getString("sender").equals(TargetID)){
                                        Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                                        msgList.add(msg);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println(e.toString());
                            }
                        }
                    }).start();
                    //adapter = new MsgAdapter(MessageActivity.this, R.layout.msg_item, msgList);
                    break;
                default:
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    };
```
### 标签界面
使用了自定义的流式布局，详情请看**com.MobileCourse.Adapter.FlowLayout**
## 后端文档
+ 管理员账号：admin    &emsp; 密码：123456
+ 学生账号：stutest1    &emsp;密码：123456
+ 老师账号：test1   &emsp;密码：123456
### 1 登录

#### 1) 请求地址

>http://47.99.112.121/login

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|user_id|账号|string|Y|-|
|password|密码|string|Y|-|

#### 4) 请求返回结果:

```python
HttpResponse（1）  登录成功
HttpResponse（-1） 账号或密码错误
```
### 2 注册

#### 1) 请求地址

>http://47.99.112.121/register

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|user_id|账号|string|Y|-|
|password|密码|string|Y|-|
|school|学校|string|Y|-|
|name|姓名|string|Y|-|
|identity|身份|string|Y|-|
#### 4) 请求返回结果:

```
HttpResponse（“注册成功”）  
HttpResponse（“账号被使用”） 
```

### 3 设置研究方向

#### 1) 请求地址

>http://47.99.112.121/setKeyword

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|user_id|账号|string|Y|-|
|identity|身份|string|Y|-|
|keyword|研究方向|string|Y|-|
|type|类型|int|Y|1为添加，2为删除，3为清空|
#### 4) 请求返回结果:

```
HttpResponse（“success”）    添加成功  
HttpResponse（“delete”）     删除成功 
```

### 4 搜索

#### 1) 请求地址

>http://47.99.112.121/search

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|identity|身份|string|Y|-|
|keyword|研究方向|string|Y|-|
|type|类型|int|Y|1为研究方向，2为姓名，3为id|
#### 4) 请求返回结果:

```python
List=[
{
 		"id": var.user_id,
        "name": var.name,
        "major": var.major,
        "class": var.grade,
        "url": var.photo_url
}
]
```
### 5 推荐

#### 1) 请求地址

>http://47.99.112.121/recommend

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|user_id|账号|string|Y|-|
|identity|身份|string|Y|-|

#### 4) 推荐算法
   采用分词匹配算法，匹配词条数最多的对象得分最高，再将list按照得分数进行排列，即可得到与用户相关程度递减的推荐队列

#### 5) 请求返回结果:

```python
List=[
{
 		"point": point,
        "id": var.user_id,
        "name": var.name,
        "major": var.major,
        "info": var.experience,
        "url": var.photo_url
}
]
```

### 6 关注

#### 1) 请求地址

>http://47.99.112.121/followteacher

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|identity|身份|string|Y|-|
|user_id|用户|string|Y|-|
|aim_id|对象|string|Y|-|

#### 4) 请求返回结果:

```python
a{
	"isfollow":1
}
```

### 7 头像

#### 1) 请求地址

>http://47.99.112.121/uploadimg

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|identity|身份|string|Y|-|
|user_id|用户|string|Y|-|
|img|图片|string|Y|base64编码|

#### 4) 请求返回结果:

```
HttpResponse("success")
```

### 8 发送消息

#### 1) 请求地址

>http://47.99.112.121/send

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|sender|发送者|string|Y|-|
|reciver|接受者|string|Y|-|
|content|内容|string|Y|-|

#### 4) 请求返回结果:

```
HttpResponse("send message")
```

### 9 接收消息

#### 1) 请求地址

>http://47.99.112.121/recive

#### 2) 调用方式：HTTP post

#### 3) 请求参数:

#### POST参数:
|字段名称       |字段说明         |类型            |必填            |备注     |
| -------------|:--------------:|:--------------:|:--------------:| ------:|
|user_id|用户|string|Y|-|
|aim_id|对方|string|Y|-|

### 4) 请求返回结果:

```python
a{
	"time": datetime.datetime.strftime(msg_List[0].data_time, '%Y-%m-%d %H:%M:%S'),
    "sender": msg_List[0].send_user,
    "content": msg_List[0].content
}
```

### 10 其他接口
### 1）关注列表
>http://47.99.112.121/followlist

#### 2) 研究方向列表
>http://47.99.112.121/kwlist

#### 3) 消息列表
>http://47.99.112.121/message_list

#### 4) 用户信息
>http://47.99.112.121/user_info

#### 5) 详情
>http://47.99.112.121/details

#### 6) 修改信息
>http://47.99.112.121/changeinfo

#### 7) 修改密码
>http://47.99.112.121/changepassword