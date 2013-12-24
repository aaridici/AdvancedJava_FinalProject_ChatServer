-- phpMyAdmin SQL Dump
-- version 2.8.0.1
-- http://www.phpmyadmin.net
-- 
-- Host: custsqlmoo09
-- Generation Time: Jun 03, 2012 at 03:09 PM
-- Server version: 5.0.91
-- PHP Version: 4.4.9
-- 
-- Database: `chat`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `chat`
-- 

CREATE TABLE `chat` (
  `id` int(11) NOT NULL auto_increment,
  `from` varchar(255) NOT NULL,
  `to` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `when` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `read` varchar(10) NOT NULL default 'unread',
  PRIMARY KEY  (`id`),
  KEY `from` (`from`,`to`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

-- 
-- Dumping data for table `chat`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `users`
-- 

CREATE TABLE `users` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(250) NOT NULL,
  `profile_image` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- 
-- Dumping data for table `users`
-- 

INSERT INTO `users` VALUES (3, 'test', 'uploads/02-2011-lexus-lfa-review.jpg');
INSERT INTO `users` VALUES (2, 'arda', 'uploads/03-2012-ferrari-ff-1302584944_1338747493.jpg');
