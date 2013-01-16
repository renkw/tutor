/*
 * File   Db4oConfigurator.java
 * Create 2013/01/13
 * Copyright (c) change-v.com 2012 
 */
package com.changev.tutor.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.config.CacheConfiguration;
import com.db4o.config.CommonConfiguration;
import com.db4o.config.ConfigScope;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.FileConfiguration;
import com.db4o.config.FreespaceFiller;
import com.db4o.config.IdSystemConfiguration;
import com.db4o.config.IdSystemFactory;
import com.db4o.config.ObjectClass;
import com.db4o.config.ObjectField;
import com.db4o.config.ObjectTranslator;
import com.db4o.config.QueryEvaluationMode;
import com.db4o.config.TypeAlias;
import com.db4o.config.WildcardAlias;
import com.db4o.config.encoding.StringEncodings;
import com.db4o.constraints.UniqueFieldValueConstraint;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.NetworkingConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.io.BlockAwareBinWindow;
import com.db4o.io.CachingStorage;
import com.db4o.io.ConstantGrowthStrategy;
import com.db4o.io.FileStorage;
import com.db4o.io.MemoryStorage;
import com.db4o.io.NonFlushingStorage;
import com.db4o.io.PagingMemoryStorage;
import com.db4o.io.Storage;
import com.db4o.ta.DeactivatingRollbackStrategy;
import com.db4o.ta.RollbackStrategy;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

/**
 * <p>
 * 根据属性文件配置db4o。
 * </p>
 * 
 * @author ren
 * 
 */
public final class Db4oConfigurator {

	public static EmbeddedConfiguration embed(Properties props,
			EmbeddedConfiguration config) {
		common(props, config.common());
		file(props, config.file());
		id(props, config.idSystem());
		return config;
	}

	public static EmbeddedConfiguration embed(Properties props) {
		return embed(props, Db4oEmbedded.newConfiguration());
	}

	public static ServerConfiguration server(Properties props,
			ServerConfiguration config) {
		// TODO
		common(props, config.common());
		file(props, config.file());
		network(props, config.networking());
		cache(props, config.cache());
		id(props, config.idSystem());
		return config;
	}

	public static ServerConfiguration server(Properties props) {
		return server(props, Db4oClientServer.newServerConfiguration());
	}

	public static ClientConfiguration client(Properties props,
			ClientConfiguration config) {
		// TODO
		common(props, config.common());
		network(props, config.networking());
		return config;
	}

	public static ClientConfiguration client(Properties props) {
		return client(props, Db4oClientServer.newClientConfiguration());
	}

	public static ObjectContainer open(Properties props) {
		String type = strValue(props, "type");
		if ("Embed".equalsIgnoreCase(type)) {
			EmbeddedConfiguration config = embed(props);
			return Db4oEmbedded.openFile(config, strValue(props, "embed.file"));
		}
		if ("Client".equalsIgnoreCase(type)) {
			ClientConfiguration config = client(props);
			return Db4oClientServer.openClient(config,
					strValue(props, "client.host"),
					intValue(props, "client.port"),
					strValue(props, "client.user"),
					strValue(props, "client.password"));
		}
		if ("Server".equalsIgnoreCase(type)) {
			ServerConfiguration config = server(props);
			ObjectServer server = Db4oClientServer.openServer(config,
					strValue(props, "server.file"),
					intValue(props, "server.port"));
			return server.openClient();
		}
		throw new IllegalArgumentException("type = " + type);
	}

	static void common(Properties props, CommonConfiguration config) {
		if (props.containsKey("common.activationDepth"))
			config.activationDepth(intValue(props, "common.activationDepth"));
		if (props.containsKey("common.updateDepth"))
			config.updateDepth(intValue(props, "common.updateDepth"));
		if (props.containsKey("common.allowVersionUpdates"))
			config.allowVersionUpdates(boolValue(props,
					"common.allowVersionUpdates"));
		if (props.containsKey("common.automaticShutdown"))
			config.automaticShutDown(boolValue(props,
					"common.automaticShutdown"));
		if (props.containsKey("common.bTreeNodeSize"))
			config.bTreeNodeSize(intValue(props, "common.bTreeNodeSize"));
		if (props.containsKey("common.callbacks"))
			config.callbacks(boolValue(props, "common.callbacks"));
		if (props.containsKey("common.callConstructors"))
			config.callConstructors(boolValue(props, "common.callConstructors"));
		if (props.containsKey("common.detectSchemaChanges"))
			config.detectSchemaChanges(boolValue(props,
					"common.detectSchemaChanges"));
		if (props.containsKey("common.exceptionsOnNotStorable"))
			config.exceptionsOnNotStorable(boolValue(props,
					"common.exceptionsOnNotStorable"));
		if (props.containsKey("common.internStrings"))
			config.internStrings(boolValue(props, "common.internStrings"));
		if (props.containsKey("common.markTransient")) {
			for (String name : strArray(props, "commom.markTransient"))
				config.markTransient(name.trim());
		}
		if (props.containsKey("common.messageLevel")) {
			String v = strValue(props, "common.messageLevel");
			if ("None".equalsIgnoreCase(v))
				config.messageLevel(0);
			else if ("Normal".equalsIgnoreCase(v))
				config.messageLevel(1);
			else if ("State".equalsIgnoreCase(v))
				config.messageLevel(2);
			else if ("Activation".equalsIgnoreCase(v))
				config.messageLevel(3);
		}
		if (props.containsKey("common.maxStackDepth"))
			config.maxStackDepth(intValue(props, "common.maxStackDepth"));
		if (props.containsKey("common.optimizeNativeQueries"))
			config.optimizeNativeQueries(boolValue(props,
					"common.optimizeNativeQueries"));
		if (props.containsKey("common.evaluationMode")) {
			String v = strValue(props, "common.evaluationMode");
			if ("Immediate".equalsIgnoreCase(v))
				config.queries().evaluationMode(QueryEvaluationMode.IMMEDIATE);
			else if ("Lazy".equalsIgnoreCase(v))
				config.queries().evaluationMode(QueryEvaluationMode.LAZY);
			else if ("Snapshot".equalsIgnoreCase(v))
				config.queries().evaluationMode(QueryEvaluationMode.SNAPSHOT);
		}
		if (props.containsKey("common.stringEncoding")) {
			String v = strValue(props, "common.stringEncoding");
			if ("utf8".equalsIgnoreCase(v))
				config.stringEncoding(StringEncodings.utf8());
			else if ("unicode".equalsIgnoreCase(v))
				config.stringEncoding(StringEncodings.unicode());
			else if ("latin".equalsIgnoreCase(v))
				config.stringEncoding(StringEncodings.latin());
		}
		if (props.containsKey("common.weakReferenceCollectionInterval"))
			config.weakReferenceCollectionInterval(intValue(props,
					"common.weakReferenceCollectionInterval") * 1000);
		if (props.containsKey("common.weakReferences"))
			config.weakReferences(boolValue(props, "common.weakReferences"));
		if (props.containsKey("common.TransparentActivationSupport")
				&& boolValue(props, "common.TransparentActivationSupport"))
			config.add(new TransparentActivationSupport());
		if (props.containsKey("common.TransparentPersistenceSupport")) {
			String v = strValue(props, "common.TransparentPersistenceSupport");
			try {
				if ("Nothing".equalsIgnoreCase(v))
					config.add(new TransparentPersistenceSupport());
				else if ("Deactivate".equalsIgnoreCase(v))
					config.add(new TransparentPersistenceSupport(
							new DeactivatingRollbackStrategy()));
				else
					config.add(new TransparentPersistenceSupport(
							(RollbackStrategy) Class.forName(v).newInstance()));
			} catch (InstantiationException e) {
				throw new IllegalArgumentException(
						"common.TransparentPersistenceSupport = " + v, e);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(
						"common.TransparentPersistenceSupport = " + v, e);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(
						"common.TransparentPersistenceSupport = " + v, e);
			}
		}
		if (props.containsKey("common.UniqueFieldValueConstraint")) {
			String v = strValue(props, "common.UniqueFieldValueConstraint");
			int i = v.lastIndexOf(':');
			if (i == -1)
				throw new IllegalArgumentException(
						"common.UniqueFieldValueConstraint = " + v);
			config.add(new UniqueFieldValueConstraint(v.substring(0, i), v
					.substring(i + 1)));
		}

		for (Enumeration<?> en = props.keys(); en.hasMoreElements();) {
			String key = (String) en.nextElement();
			if (!key.startsWith("common."))
				continue;
			String v = strValue(props, key);
			if (key.startsWith("alias.", 7)) {
				if (v.indexOf('*') == -1)
					config.addAlias(new TypeAlias(key.substring(13), v));
				else
					config.addAlias(new WildcardAlias(key.substring(13), v));
			} else if (key.startsWith("class.", 7)) {
				int i = key.indexOf(':');
				int j = key.indexOf('.', i + 1);
				ObjectClass cls = config.objectClass(key.substring(13,
						i != -1 ? i : j));
				ObjectField fld = i == -1 ? null : cls.objectField(key
						.substring(i + 1, j));
				try {
					classField(cls, fld, key.substring(j + 1), v);
				} catch (InstantiationException e) {
					throw new IllegalArgumentException(key + " = " + v, e);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException(key + " = " + v, e);
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException(key + " = " + v, e);
				}
			}
		}
	}

	static void classField(ObjectClass cls, ObjectField fld, String key,
			String value) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (fld == null) {
			if ("callConstructor".equals(key))
				cls.callConstructor(Boolean.parseBoolean(value));
			else if ("cascadeOnDelete".equals(key))
				cls.cascadeOnDelete(Boolean.parseBoolean(value));
			else if ("cascadeOnUpdate".equals(key))
				cls.cascadeOnUpdate(Boolean.parseBoolean(value));
			else if ("cascadeOnActivate".equals(key))
				cls.cascadeOnActivate(Boolean.parseBoolean(value));
			else if ("indexed".equals(key))
				cls.indexed(Boolean.parseBoolean(value));
			else if ("generateUUIDs".equals(key))
				cls.generateUUIDs(Boolean.parseBoolean(value));
			else if ("maximumActivationDepth".equals(key))
				cls.maximumActivationDepth(Integer.parseInt(value));
			else if ("minimumActivationDepth".equals(key))
				cls.minimumActivationDepth(Integer.parseInt(value));
			else if ("persistStaticFieldValues".equals(key)) {
				if (Boolean.parseBoolean(value))
					cls.persistStaticFieldValues();
			} else if ("rename".equals(key))
				cls.rename(value);
			else if ("translate".equals(key)) {
				cls.translate((ObjectTranslator) Class.forName(value)
						.newInstance());
			} else if ("storeTransientFields".equals(key))
				cls.storeTransientFields(Boolean.parseBoolean(value));
			else if ("updateDepth".equals(key))
				cls.updateDepth(Integer.parseInt(value));
		} else {
			if ("indexed".equals(key))
				fld.indexed(Boolean.parseBoolean(value));
			else if ("cascadeOnActivate".equals(key))
				fld.cascadeOnActivate(Boolean.parseBoolean(value));
			else if ("cascadeOnUpdate".equals(key))
				fld.cascadeOnUpdate(Boolean.parseBoolean(value));
			else if ("cascadeOnDelete".equals(key))
				fld.cascadeOnDelete(Boolean.parseBoolean(value));
			else if ("rename".equals(key))
				fld.rename(value);
		}
	}

	static void file(Properties props, FileConfiguration config) {
		if (props.containsKey("file.asynchronousSync"))
			config.asynchronousSync(boolValue(props, "file.asynchronousSync"));
		if (props.containsKey("file.blobPath")) {
			String v = strValue(props, "file.blobPath");
			try {
				config.blobPath(v);
			} catch (IOException e) {
				throw new IllegalArgumentException("file.blobPath = " + v, e);
			}
		}
		if (props.containsKey("file.blockSize"))
			config.blockSize(intValue(props, "file.blockSize"));
		if (props.containsKey("file.databaseGrowthSize"))
			config.databaseGrowthSize(intValue(props, "file.databaseGrowthSize"));
		if (props.containsKey("file.disableCommitRecovery")
				&& boolValue(props, "file.disableCommitRecovery"))
			config.disableCommitRecovery();
		if (props.containsKey("file.discardSmallerThan"))
			config.freespace().discardSmallerThan(
					intValue(props, "file.discardSmallerThan"));
		if (props.containsKey("file.freespaceSystem")) {
			String v = strValue(props, "file.freespaceSystem");
			if ("Ram".equalsIgnoreCase(v))
				config.freespace().useRamSystem();
			else if ("BTree".equalsIgnoreCase(v))
				config.freespace().useBTreeSystem();
		}
		if (props.containsKey("file.fillDeleted")
				&& boolValue(props, "file.fillDeleted")) {
			config.freespace().freespaceFiller(new FreespaceFiller() {

				byte[] buf = new byte[1024];

				@Override
				public void fill(BlockAwareBinWindow io) throws IOException {
					for (int i = 0, len = io.length(); i < len; i += buf.length) {
						if (i + buf.length > len)
							io.write(i, new byte[len - i]);
						else
							io.write(i, buf);
					}
				}
			});
		}
		if (props.containsKey("file.generateUUIDs")) {
			String v = strValue(props, "file.generateUUIDs");
			if ("Globally".equalsIgnoreCase(v))
				config.generateUUIDs(ConfigScope.GLOBALLY);
			else if ("Individually".equalsIgnoreCase(v))
				config.generateUUIDs(ConfigScope.INDIVIDUALLY);
			else if ("Disabled".equalsIgnoreCase(v))
				config.generateUUIDs(ConfigScope.DISABLED);
		}
		if (props.containsKey("file.generateCommitTimestamps"))
			config.generateCommitTimestamps(boolValue(props,
					"file.generateCommitTimestamps"));
		if (props.containsKey("file.lockDatabaseFile"))
			config.lockDatabaseFile(boolValue(props, "file.lockDatabaseFile"));
		if (props.containsKey("file.readOnly"))
			config.readOnly(boolValue(props, "file.readOnly"));
		if (props.containsKey("file.recoveryMode"))
			config.recoveryMode(boolValue(props, "file.recoveryMode"));
		if (props.containsKey("file.reserveStorageSpace"))
			config.reserveStorageSpace(intValue(props,
					"file.reserveStorageSpace"));
		if (props.containsKey("file.storage")) {
			String[] v = StringUtils
					.split(strValue(props, "file.storage"), '>');
			Storage storage = null;
			for (int i = 0; i < v.length; i++) {
				if ("File".equalsIgnoreCase(v[i])) {
					storage = new FileStorage();
				} else if ("Cache".equalsIgnoreCase(v[i])) {
					String size = strValue(props, "file.storage.cache");
					if (size == null) {
						storage = new CachingStorage(storage);
					} else {
						int at = size.indexOf('*');
						storage = new CachingStorage(storage,
								Integer.parseInt(size.substring(0, at)),
								Integer.parseInt(size.substring(at + 1)));
					}
				} else if ("Memory".equalsIgnoreCase(v[i])) {
					String size = strValue(props, "file.storage.cache");
					if (size == null) {
						storage = new MemoryStorage();
					} else {
						storage = new MemoryStorage(new ConstantGrowthStrategy(
								Integer.parseInt(size)));
					}
				} else if ("PagingMemory".equalsIgnoreCase(v[i])) {
					String size = strValue(props, "file.storage.paging");
					if (size == null) {
						storage = new PagingMemoryStorage();
					} else {
						storage = new PagingMemoryStorage(
								Integer.parseInt(size));
					}
				} else if ("NonFlush".equalsIgnoreCase(v[i])) {
					storage = new NonFlushingStorage(storage);
				} else {
					try {
						Class<?> cls = Class.forName(v[i]);
						Constructor<?> c = null;
						try {
							c = cls.getConstructor(Storage.class, String.class);
						} catch (NoSuchMethodException e) {
							try {
								c = cls.getConstructor(Storage.class);
							} catch (NoSuchMethodException e1) {
							}
						}
						if (c == null)
							storage = (Storage) cls.newInstance();
						else if (c.getParameterTypes().length == 1)
							storage = (Storage) c.newInstance(storage);
						else
							storage = (Storage) c.newInstance(storage,
									strValue(props, "file.storage." + v[i]));
					} catch (ClassNotFoundException e) {
						throw new IllegalArgumentException("file.storage = "
								+ v[i], e);
					} catch (InstantiationException e) {
						throw new IllegalArgumentException("file.storage = "
								+ v[i], e);
					} catch (IllegalAccessException e) {
						throw new IllegalArgumentException("file.storage = "
								+ v[i], e);
					} catch (InvocationTargetException e) {
						throw new IllegalArgumentException("file.storage = "
								+ v[i], e);
					}
				}
			}
			config.storage(storage);
		}
	}

	static void id(Properties props, IdSystemConfiguration config) {
		if (props.containsKey("id.system")) {
			String v = strValue(props, "id.system");
			if ("Memory".equalsIgnoreCase(v))
				config.useInMemorySystem();
			else if ("Stacked".equalsIgnoreCase(v))
				config.useStackedBTreeSystem();
			else if ("Single".equalsIgnoreCase(v))
				config.useSingleBTreeSystem();
			else if ("Pointer".equalsIgnoreCase(v))
				config.usePointerBasedSystem();
			else {
				try {
					config.useCustomSystem((IdSystemFactory) Class.forName(v)
							.newInstance());
				} catch (InstantiationException e) {
					throw new IllegalArgumentException("id.system = " + v, e);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("id.system = " + v, e);
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("id.system = " + v, e);
				}
			}
		}
	}

	static void network(Properties props, NetworkingConfiguration config) {
		// TODO
	}

	static void cache(Properties props, CacheConfiguration config) {
		// TODO
	}

	static String strValue(Properties props, String key) {
		String v = props.getProperty(key, "");
		int rep1;
		if (!v.isEmpty() && (rep1 = v.indexOf('{')) != -1) {
			StringBuilder sb = new StringBuilder(v);
			do {
				int rep2 = sb.indexOf("}", rep1 + 1);
				if (rep2 == -1)
					break;
				String var = v.substring(rep1 + 1, rep2).trim();
				if ("{".equals(var))
					var = "{";
				else if (var.startsWith("$"))
					var = System.getProperty(var.substring(1));
				else
					var = strValue(props, var);
				sb.replace(rep1, rep2 + 1, var);
				rep1 += var.length();
			} while ((rep1 = sb.indexOf("{", rep1)) != -1);
			v = sb.toString();
		}
		return v;
	}

	static boolean boolValue(Properties props, String key) {
		return Boolean.parseBoolean(strValue(props, key));
	}

	static int intValue(Properties props, String key) {
		return Integer.parseInt(strValue(props, key));
	}

	static String[] strArray(Properties props, String key) {
		return StringUtils.split(strValue(props, key), ',');
	}

}
