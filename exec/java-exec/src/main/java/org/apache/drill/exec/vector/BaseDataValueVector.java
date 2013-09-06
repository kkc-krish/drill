package org.apache.drill.exec.vector;

import io.netty.buffer.ByteBuf;

import org.apache.drill.exec.memory.BufferAllocator;
import org.apache.drill.exec.proto.UserBitShared.FieldMetadata;
import org.apache.drill.exec.record.DeadBuf;
import org.apache.drill.exec.record.MaterializedField;

public abstract class BaseDataValueVector extends BaseValueVector{

  protected ByteBuf data = DeadBuf.DEAD_BUFFER;
  protected int valueCount;
  
  public BaseDataValueVector(MaterializedField field, BufferAllocator allocator) {
    super(field, allocator);
    
  }

  /**
   * Release the underlying ByteBuf and reset the ValueVector
   */
  @Override
  public void clear() {
    if (data != DeadBuf.DEAD_BUFFER) {
      data.release();
      data = DeadBuf.DEAD_BUFFER;
      valueCount = 0;
    }
  }

  
  @Override
  public ByteBuf[] getBuffers(){
    ByteBuf[] out;
    if(valueCount == 0){
      out = new ByteBuf[0];
    }else{
      out = new ByteBuf[]{data};
      data.readerIndex(0);
      data.retain();
    }
    clear();
    return out;
  }
  
  public int getBufferSize() {
    if(valueCount == 0) return 0;
    return data.writerIndex();
  }

  @Override
  public abstract FieldMetadata getMetadata();

  public ByteBuf getData(){
    return data;
  }
  
  
}
