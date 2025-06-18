# No Circle

## 服务端状态码

### 10xx - 用户相关

- 100x: [用户登录](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserLogin.kt)
- 101x: [用户登出](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserLogout.kt)
- 102x: [用户详情](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserDetail.kt)
- 103X: [用户注册](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/user/UserRegister.kt)

### 11xx - 用户标签相关

- 110x: [添加标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelAdd.kt)
- 111x: [删除标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelDelete.kt)
- 112x: [查询标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelQuery.kt)
- 113x: [更新标签](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/label/LabelUpdate.kt)

### 12xx - 好友相关

- 120x: [搜索好友](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/FriendSearch.kt)

### 13xx - 好友请求相关
- 130x: [添加好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/FriendSearch.kt)
- 131x: [查询好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/request/FriendRequestQuery.kt)
- 132x: [取消好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/request/FriendRequestCancel.kt)
- 133x: [删除好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/request/FriendRequestDelete.kt)
- 134x: [拒绝好友请求](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/request/FriendRequestReject.kt)
- 135x: [查询未处理请求数](no-circle-server-app/src/main/kotlin/com/nocircle/server/app/routes/friend/request/FriendRequestQueryWaitingCount.kt)