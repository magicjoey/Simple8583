package com.simple8583.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.simple8583.key.SimpleConstants;
import com.simple8583.util.EncodeUtil;
import com.simple8583.util.SimpleUtil;

/**
 * <p>报文域详情.</p>
 *
 * @author Magic Joey
 * @version IsoField.java 1.0 Created@2014-07-10 15:49 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IsoField implements Serializable{

	@XmlAttribute(name = "id", required = true)
	private String id;

	@XmlAttribute(name = "type", required = true)
	private String type;

	//该属性为字节长度
	@XmlAttribute(name = "length")
	private int length = 0;

	// 字符值的value
	private String value;

	// 字节数组值的value
	private byte[] byteValue;

	// 域类型
	private IsoType isoType;

	// 该域是否被使用
	private boolean checked = false;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// 获取本域的IsoType
	public IsoType getIsoType() {
		if (this.isoType == null) {
			this.isoType = IsoType.valueOf(this.type);
		}
		return isoType;
	}

	public String getValue() {
		return value;
	}

	public boolean isChecked() {
		return checked;
	}

	public byte[] getByteValue() {
		return byteValue;
	}

	public void setByteValue(byte[] bts) throws UnsupportedEncodingException {
		this.byteValue = bts;
		this.checked = true;
		switch (this.getIsoType()) {
		case BINARY:
			this.value = EncodeUtil.binary(bts);
			break;
		case NUMERIC:
		case LLVAR_NUMERIC:
			this.value = EncodeUtil.hex(bts);
			break;
		case CHAR:
		case LLVAR:
		case LLLVAR:
			this.value = new String(bts, SimpleConstants.ENCODING);
			break;
		default:
			this.checked = false;// 无效设置
			break;
		}
	}

	public void setValue(String value) throws UnsupportedEncodingException {
		// 应用数据域被选中
		this.checked = true;
		this.value = value;
		// 格式化
		format(this.value, this.length);
		if (this.value != null) {
			switch (this.getIsoType()) {
			case BINARY:
				this.byteValue = EncodeUtil.binary(this.value);
				break;
			case NUMERIC:
			case LLVAR_NUMERIC:
				this.byteValue = EncodeUtil.bcd(this.value);
				break;
			case CHAR:
			case LLVAR:
			case LLLVAR:
				this.byteValue = this.value.getBytes(SimpleConstants.ENCODING);
				break;
			default:
				this.checked = false;
				break;
			}
		}
	}

	public void format(String value, int length)
			throws UnsupportedEncodingException {
		if (this.isoType == null) {
			this.isoType = IsoType.valueOf(this.type);
		}
		switch (this.isoType) {
		case CHAR: {
			if (value.length() > length) {
				this.value = this.value.substring(0, length);
			} else if (value.length() < length) {
				//将缺少的部分补全空格
				this.value = EncodeUtil.addBlankRight(this.value, this.length
						- value.getBytes(SimpleConstants.ENCODING).length, " ");
			}
			break;
		}
		// LLVAR和LLLVAR类型的数据不格式化
		case LLVAR:
		case LLLVAR:
			break;
		case LLVAR_NUMERIC:
			if (this.value.length() % 2 == 1) {
				this.value = EncodeUtil.addBlankLeft(this.value, 1, "0");
			}
			break;
		case NUMERIC: {
			if (this.value.length() > length * 2) {
				throw new IllegalArgumentException("数据域 " + this.id
						+ "长度超出，值为:" + this.value + "，约定长度" + length);
			} else {
				this.value = EncodeUtil.addBlankLeft(this.value, length * 2
						- this.value.length(), "0");
			}
			break;
		}
		case BINARY: {
			int len = this.value.length();
			if (len < 8 * this.length) {
				this.value = EncodeUtil.addBlankLeft(this.value, 8 * length
						- len, "0");
			} else if (len > 8 * this.length) {
				this.value = this.value.substring(0, this.length * 8);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("不支持的参数类型：" + this.isoType);
		}
	}

	@Override
	public String toString() {
		StringBuilder accum = new StringBuilder();
		accum.append("id=").append(this.id ).append( ",type=" ).append( this.type ).append( ",value="
				).append( this.value);
		return accum.toString();
	}

	//该域是否为1~64/1~128的数据域
	public boolean isAppData() {
		return SimpleUtil.isNumeric(this.id);
	}

}
