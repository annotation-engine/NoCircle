# No Circle

## 服务端状态码

### 10xx - 用户

- 100x: [用户登录](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserLogin.kt)
- 101x: [用户登出](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserLogout.kt)
- 102x: [用户详情](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserDetail.kt)
- 103X: [用户注册](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserRegister.kt)

### 11xx - 标签

- 110x: [添加标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelAdd.kt)
- 111x: [删除标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelDelete.kt)
- 112x: [查询标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelQuery.kt)
- 113x: [更新标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelUpdate.kt)

### 12xx - 好友

- 120x: [搜索好友](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/FriendSearch.kt)
- 121x: [添加好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/FriendAddRequest.kt)