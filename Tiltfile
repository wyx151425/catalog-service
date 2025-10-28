# 构建
custom_build(
    # 容器镜像的名称
    ref = 'catalog-service',
    # 构建容器镜像的命令
    # 在Windows系统上，将$EXPECTED_REF替换为%EXPECTED_REF%
    command = './gradlew bootBuildImage --imageName %EXPECTED_REF%',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)

# 部署
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# 管理
k8s_resource('catalog-service', port_forwards=['9001'])
