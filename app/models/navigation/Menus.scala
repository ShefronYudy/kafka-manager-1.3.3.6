/**
 * Copyright 2015 Yahoo Inc. Licensed under the Apache License, Version 2.0
 * See accompanying LICENSE file.
 */

package models.navigation

import features.{KMTopicManagerFeature, KMClusterManagerFeature, ApplicationFeatures}
import kafka.manager.features.{KMLogKafkaFeature, ClusterFeatures}

/**
 * @author hiral
 */
class Menus(implicit applicationFeatures: ApplicationFeatures) {
  import models.navigation.QuickRoutes._
  
  private[this] def clusterMenu(cluster: String) : Option[Menu] = {
    val defaultItems = IndexedSeq("概要".clusterRouteMenuItem(cluster),
                                  "列表".baseRouteMenuItem)
    val items = {
      if(applicationFeatures.features(KMClusterManagerFeature))
        defaultItems.+:("添加集群".baseRouteMenuItem)
      else
        defaultItems
    }
    
    Option(Menu("集群", items, None))
  }

  private[this] def topicMenu(cluster: String) : Option[Menu] = {
    val defaultItems = IndexedSeq("列表".clusterRouteMenuItem(cluster))
    
    val items = {
      if(applicationFeatures.features(KMTopicManagerFeature))
        defaultItems.+:("创建".clusterRouteMenuItem(cluster))
      else
        defaultItems
    }

    Option(Menu("主题", items, None))
  }
  
  private[this] def brokersMenu(cluster: String) : Option[Menu] = {
    Option("Broker列表".clusterMenu(cluster))
  }
  
  private[this] def preferredReplicaElectionMenu(cluster: String) : Option[Menu] = {
    Option("首选副本选举".clusterMenu(cluster))
  }
  
  private[this] def reassignPartitionsMenu(cluster: String) : Option[Menu] = {
    Option("重新分配分区".clusterMenu(cluster))
  }

  private[this] def consumersMenu(cluster: String) : Option[Menu] = {
    Option("消费者列表".clusterMenu(cluster))
  }
  
  private[this] def logKafkaMenu(cluster: String, 
                                 clusterFeatures: ClusterFeatures) : Option[Menu] = {
    if (clusterFeatures.features(KMLogKafkaFeature)) {
      Option(Menu("Logkafka", IndexedSeq(
        "Logkafka列表".clusterRouteMenuItem(cluster),
        "创建Logkafka".clusterRouteMenuItem(cluster)),
                  None))
    } else None
  }
  
  def clusterMenus(cluster: String)
                  (implicit clusterFeatures: ClusterFeatures) : IndexedSeq[Menu] = {
    IndexedSeq(
      clusterMenu(cluster),
      brokersMenu(cluster),
      topicMenu(cluster),
      preferredReplicaElectionMenu(cluster),
      reassignPartitionsMenu(cluster),
      consumersMenu(cluster),
      logKafkaMenu(cluster, clusterFeatures)
    ).flatten
  }
  
  def indexMenu = {
    val defaultItems = IndexedSeq("列表".baseRouteMenuItem)
    val items = {
      if(applicationFeatures.features(KMClusterManagerFeature))
        defaultItems.+:("添加集群".baseRouteMenuItem)
      else
        defaultItems
    }
    IndexedSeq(Menu("集群", items, None))
  }
}
