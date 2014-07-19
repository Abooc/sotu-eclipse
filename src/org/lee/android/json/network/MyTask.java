package org.lee.android.json.network;



public class MyTask {
//	public static interface OnSaveToServerListener {
//
//		public void onSaveFinished(int resultCode);
//
//	}
//
//	public static interface OnLoadListener {
//
//		public void onLoadStart();
//
//		public void onLoadFinish(int resultCode, ArrayList<?> array);
//
//	}
//
//	public static interface OnLoadImageListener {
//
//		public void onLoadImageStart(LoadTask task);
//
//		public void onLoadImageFinish(int resultCode, LoadTask task,
//				Drawable drawable);
//
//	}
//
//	private Timer mTimer = new Timer();
//
//	public void start2() {
//		if (isWatting) {
//			return;
//		}
//		isWatting = true;
//		if (mTaskQueue.size() == 0) {
//			return;
//		}
//		mTimer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				while (mTaskQueue.hasNext()) {
//					LoadTask task = (LoadTask) mTaskQueue.next();
//					if (task == null) {
//						continue;
//					}
//					Message msg = Message.obtain(mResultHandler, START,
//							new Resault(0, task.iOnLoadImageListener, null));
//					mResultHandler.sendMessage(msg);
//
//					Drawable d = null;
//					int resultCode = 0;
//					if (!TextUtils.isEmpty(task.url)) {
//						try {
//							byte[] data = WrapperTools.getImage(task.url);
//							if (data != null) {
//								Bitmap bitmap = BitmapFactory.decodeByteArray(
//										data, 0, data.length);
//								d = new BitmapDrawable(mContext.getResources(),
//										bitmap);
//								resultCode = 200;
//							} else {
//								resultCode = -1;
//								android.util.Log.e("Exception",
//										"data == null, " + task.url);
//							}
//						} catch (Exception e) {
//							resultCode = -1;
//							android.util.Log.e("Exception", task.url + ", e:"
//									+ e);
//						}
//					}
//					Resault result = new Resault(resultCode,
//							task.iOnLoadImageListener, d);
//					mResultHandler.handleResult(task, result);
//				}
//				isWatting = false;
//			}
//		}, 0);
//	}
//
//	/**
//	 * 图片下载任务队列的观察器，监听任务队列的改变
//	 * 
//	 * @author ruiyuLee
//	 * 
//	 */
//	private interface OnDataSetChangeObserver {
//		void onAdd();
//
//		void onRemove();
//	}
//
//	/**
//	 * 任务队列，负责顺序下载图片
//	 * 
//	 * @author ruiyuLee
//	 * 
//	 * @param <E>
//	 */
//	private class TaskQueue<E> extends ArrayList<E> {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//		private OnDataSetChangeObserver iOnDataSetChangeObserver;
//
//		TaskQueue() {
//			iOnDataSetChangeObserver = new OnDataSetChangeObserver() {
//
//				@Override
//				public void onAdd() {
//					start2();
//				}
//
//				@Override
//				public void onRemove() {
//					start2();
//				}
//			};
//		}
//
//		@Override
//		public boolean add(E object) {
//			boolean b = super.add(object);
//			if (b) {
//				iOnDataSetChangeObserver.onAdd();
//			}
//			return b;
//		}
//
//		@Override
//		public boolean remove(Object object) {
//			boolean b = super.remove(object);
//			if (b) {
//				iOnDataSetChangeObserver.onRemove();
//			}
//			return b;
//		}
//
//		public boolean hasNext() {
//			return size() > 0;
//		}
//
//		/**
//		 * 获取下一个对象,索引会自动下移一位
//		 * 
//		 * @return
//		 */
//		@SuppressWarnings("unchecked")
//		public E next() {
//			E e = get(0);
//			if (e instanceof LoadTask) {
//				LoadTask task = ((LoadTask) e);
//				e = (E) task.clone();
//			}
//			remove(0);
//			return e;
//		}
//
//	}
//
//	public static class LoadTask implements Cloneable {
//		private OnLoadImageListener iOnLoadImageListener;
//		public int groupPosition;
//		public int childPosition;
//		private String url;
//
//		public LoadTask(OnLoadImageListener l) {
//			this.url = url;
//			this.iOnLoadImageListener = l;
//		}
//
//		@Override
//		public boolean equals(Object o) {
//			if (o == null) {
//				return false;
//			}
//			if (o instanceof LoadTask) {
//				LoadTask other = (LoadTask) o;
//				if (other.groupPosition == groupPosition
//						&& other.childPosition == childPosition) {
//					return true;
//				}
//			}
//			return false;
//		}
//
//		@Override
//		protected Object clone() {
//			LoadTask newObject;
//			try {
//				newObject = (LoadTask) super.clone();
//				return newObject;
//			} catch (CloneNotSupportedException e) {
//			}// Won't happen
//			return null;
//		}
//
//	}
//
//	private boolean isWatting;
//	private TaskQueue<LoadTask> mTaskQueue = new TaskQueue<LoadTask>();
//
//	private static final int START = 0;
//	private static final int FINISH = 1;
//
//	private static class ResultHandler extends Handler {
//
//		private LoadTask currentTask;
//
//		public void handleResult(LoadTask task, Resault result) {
//			currentTask = task;
//			Message msg = Message.obtain(mResultHandler, FINISH, result);
//			sendMessageAtFrontOfQueue(msg);
//			// handleMessage(msg);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			Resault r = (Resault) msg.obj;
//			switch (msg.what) {
//			case START:
//				if (r.iOnLoadListener != null) {
//					((OnLoadListener) r.iOnLoadListener).onLoadStart();
//				} else if (r.iOnLoadImageListener != null
//						&& currentTask != null) {
//					r.iOnLoadImageListener.onLoadImageStart(currentTask);
//				}
//				break;
//			case FINISH:
//				if (r.iOnLoadImageListener != null && currentTask != null) {
//					r.iOnLoadImageListener.onLoadImageFinish(r.resaultCode,
//							currentTask, r.drawable);
//				} else if (r.iOnLoadListener != null) {
//					r.iOnLoadListener.onLoadFinish(r.resaultCode, r.array);
//				} else if (r.iOnSaveToServerListener != null) {
//					r.iOnSaveToServerListener.onSaveFinished(r.resaultCode);
//				}
//				break;
//			default:
//				break;
//			}
//		}
//
//	}
//
//	private static ResultHandler mResultHandler = new ResultHandler();
	// class Resault {
	// private int resaultCode;
	// private OnSaveToServerListener iOnSaveToServerListener;
	// private OnLoadListener iOnLoadListener;
	// private OnLoadImageListener iOnLoadImageListener;
	// private ArrayList<?> array;
	// private Drawable drawable;
	//
	// public Resault(OnSaveToServerListener l, int resultCode) {
	// this.iOnSaveToServerListener = l;
	// this.resaultCode = resultCode;
	// }
	//
	// public Resault(OnLoadListener l, ArrayList<?> a) {
	// this.iOnLoadListener = l;
	// this.array = a;
	// }
	//
	// public Resault(int resultCode, OnLoadImageListener l, Drawable d) {
	// this.resaultCode = resultCode;
	// this.iOnLoadImageListener = l;
	// this.drawable = d;
	// }
	//
	// }
}
