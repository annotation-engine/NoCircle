# No Circle

## [项目架构]

### 客户端

- 采用 Kotlin Multiplatform 框架设计
- 支持 MacOS, Windows, Linux, Android, IOS

### 服务端

- 服务端框架采用 Ktor
- 数据库采用 Postgresql
- 缓存采用 Redis

## 数据库 Postgresql 版本迁移

### 1. 查询版本查看是否需要升级

```shell
psql -version
brew search postgresql
```

### 2. 下载新版本数据库「新」

```shell
brew install postgresql@17
```

### 3. 停止旧版本数据库「旧」

```shell
brew services stop postgresql@14
```

### 4. 数据库兼容性检查

```shell
$$(brew --prefix)/opt/postgresql@17/bin/pg_upgrade \
  -b $(brew --prefix)/opt/postgresql@14/bin \
  -B $(brew --prefix)/opt/postgresql@17/bin \
  -d $(brew --prefix)/var/postgresql@14 \
  -D $(brew --prefix)/var/postgresql@17 \
  --check
```

### 5. 数据迁移

```shell
$$(brew --prefix)/opt/postgresql@17/bin/pg_upgrade \
  -b $(brew --prefix)/opt/postgresql@14/bin \
  -B $(brew --prefix)/opt/postgresql@17/bin \
  -d $(brew --prefix)/var/postgresql@14 \
  -D $(brew --prefix)/var/postgresql@17
```

### 6. 链接到新版本

```shell
brew unlink postgresql@14
brew link postgresql@17
```

### 7. 启动新数据库「新」

```shell
brew services start postgresql@17
```

### 8. 确认新数据库启动「新」

```shell
psql --version
brew services list
```

### 9. 卸载旧版本数据库「旧」

```shell
brew uninstall postgresql@14
```