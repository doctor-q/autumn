namespace java cc.doctor.thrift

struct Request {
    # 传递的参数信息，使用格式进行表示
    1:required string param;
    # 服务调用者请求的服务名
    2:required string serviceName
    #具体service下执行某个方法
    3:string method
}

# 这个结构体，定义了服务提供者的返回信息
struct Response {
    1: bool success
    # RESCODE 是处理状态代码，是一个枚举类型。例如RESCODE._200表示处理成功
    2: i32 error;
    3: string errorMsg
    # 返回的处理结果，同样使用JSON格式进行描述
    4: string param;
    5: string data

}

# 这是经过泛化后的Apache Thrift接口
service ThriftService {
    Response invoke(1:required Request request);
}

