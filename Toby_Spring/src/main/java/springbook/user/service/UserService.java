package springbook.user.service;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	UserDao userDao;
	private PlatformTransactionManager transactionManager;
	private MailSender mailSender;
//	DataSource dataSource;
//	UserLevelUpgradePolicy userLevelUpgradePolicy;
//	의존성 주입 성공
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
	
//	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
//		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
//	}				UserLevelUpgradePolicy를 DI 해준 LevelUpgrade를 가져온 모습
	
	public void upgradeLevels() throws Exception {
//		TransactionSynchronizationManager.initSynchronization();
//		Connection c = DataSourceUtils.getConnection(dataSource);
//		c.setAutoCommit(false);
		
//		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);	 의존성 주입까지해서 필요 없는듯
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) upgradeLevel(user);
			}
			this.transactionManager.commit(status);
		} catch(Exception e) {
			this.transactionManager.rollback(status);
			throw e; } 
//		finally {
//			DataSourceUtils.releaseConnection(c, dataSource);
//			TransactionSynchronizationManager.unbindResource(this.dataSource);
//			TransactionSynchronizationManager.clearSynchronization();
//		}
		
//		for(User user : users) {
//			if(userLevelUpgradePolicy.canUpgradeLevel(user)) userLevelUpgradePolicy.upgradeLevel(user);
			
//			if(canUpgradeLevel(user)) upgradeLevel(user);
//		}			DI해준 LevelUpgrade를 쓰는 모습
		
//		for(User user : users) {
//			Boolean changed = null;
//			
//			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
//				user.setLevel(Level.SILVER);
//				changed = true;
//			}
//			else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
//				user.setLevel(Level.GOLD);
//				changed = true;
//			}
//			else if (user.getLevel() == Level.GOLD) changed = false;
//			else changed = false;
//			
//			if(changed) userDao.update(user);
//		}
	}
	
	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	protected boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		// 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다. 새로운 레벨이 추가되고 로직을 수정하지 않으면
		// 에러가 나서 확인할 수 있다.
		}
	}
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEmail(user);
		
//		if (user.getLevel() == Level.BASIC) user.setLevel(Level.SILVER);
//		else if (user.getLevel() == Level.SILVER) user.setLevel(Level.GOLD);
//		userDao.update(user);
	}
	
	private void sendUpgradeEmail(User user) {		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}
}
