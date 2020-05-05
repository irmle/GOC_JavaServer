package Network.RMI_Classes;


import io.netty.channel.ChannelHandlerContext;

public class RMI_OverConnectionTask implements Runnable
{
    ChannelHandlerContext ctx;
    public RMI_OverConnectionTask(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        this.ctx.close();
    }
}