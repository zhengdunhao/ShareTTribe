package com.atm.chatonline.bbs.commom;
/**
 * ���ڴ洢ϵ��ѧУ��רҵ�ļ���
 * 2015.7.24��atm--��
 */
import com.example.studentsystem01.R;

public class RegisterSet {
		//����ѧУϵ�𼯺�
		final int[] department={
				R.array.jr_school_dept,R.array.gy_school_dept
		};
		//����ѧԺ��ϵ��רҵ����
		final int[] countofJr={R.array.jr_it_major,R.array.jr_ec_major
				};
		//��ҵ��ѧ��ϵ��רҵ����
		final int[] countofGy={R.array.gy_gc_major,R.array.gy_jd_major};
		public int[] getDepartment() {
			return department;
		}
		public int[] getCountofjr() {
			return countofJr;
		}
		public int[] getCountofgy() {
			return countofGy;
		}
		
}
