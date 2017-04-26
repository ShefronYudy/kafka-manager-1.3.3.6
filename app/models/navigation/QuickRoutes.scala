/**
 * Copyright 2015 Yahoo Inc. Licensed under the Apache License, Version 2.0
 * See accompanying LICENSE file.
 */

package models.navigation

import play.api.mvc.Call

/**
 * @author hiral
 */
object QuickRoutes {
  import models.navigation.BreadCrumbs._

  val baseRoutes : Map[String, () => Call] = Map(
    "集群列表" -> controllers.routes.Application.index,
    "列表" -> controllers.routes.Application.index,
    "添加集群" -> controllers.routes.Cluster.addCluster
  )
  val clusterRoutes : Map[String, String => Call] = Map(
    "更新集群" -> controllers.routes.Cluster.updateCluster,
    "概要" -> controllers.routes.Cluster.cluster,
    "Broker列表" -> controllers.routes.Cluster.brokers,
    "Topic列表" -> controllers.routes.Topic.topics,
    "消费者列表" -> controllers.routes.Consumer.consumers,
    "列表" -> controllers.routes.Topic.topics,
    "创建" -> controllers.routes.Topic.createTopic,
    "首选副本选举" -> controllers.routes.PreferredReplicaElection.preferredReplicaElection,
    "重新分配分区" -> controllers.routes.ReassignPartitions.reassignPartitions,
    "Logkafka列表" -> controllers.routes.Logkafka.logkafkas,
    "Logkafka列表" -> controllers.routes.Logkafka.logkafkas,
    "创建Logkafka" -> controllers.routes.Logkafka.createLogkafka
  )
  val topicRoutes : Map[String, (String, String) => Call] = Map(
    "Topic视图" -> ((c, t) => controllers.routes.Topic.topic(c, t, force=false)),
    "添加分区" -> controllers.routes.Topic.addPartitions,
    "更新配置" -> controllers.routes.Topic.addPartitions
  )
  val consumerRoutes : Map[String, (String, String, String) => Call] = Map(
    "消费者视图" -> controllers.routes.Consumer.consumer
  )
  val logkafkaRoutes : Map[String, (String, String, String) => Call] = Map(
    "Logkafka视图" -> controllers.routes.Logkafka.logkafka,
    "更新配置" -> controllers.routes.Logkafka.updateConfig
  )

  implicit class BaseRoute(s: String) {
    def baseRouteMenuItem : (String, Call) = {
      s -> baseRoutes(s)()
    }
    def baseRoute : Call = {
      baseRoutes(s)()
    }
    def baseMenu(c: String): Menu = {
      Menu(s,IndexedSeq.empty,Some(baseRoute))
    }
    def baseRouteBreadCrumb : BCStaticLink = {
      BCStaticLink(s, baseRoutes(s)())
    }
  }

  implicit class ClusterRoute(s: String) {
    def clusterRouteMenuItem(c: String): (String, Call) = {
      s -> clusterRoutes(s)(c)
    }
    def clusterRoute(c: String): Call = {
      clusterRoutes(s)(c)
    }
    def clusterMenu(c: String): Menu = {
      Menu(s,IndexedSeq.empty,Some(clusterRoute(c)))
    }
    def clusterRouteBreadCrumb : BCDynamicLink = {
      BCDynamicLink( s, clusterRoutes(s))
    }
  }

  implicit class TopicRoute(s: String) {
    def topicRouteMenuItem(c: String, t: String): (String, Call) = {
      s -> topicRoutes(s)(c,t)
    }
    def topicRoute(c: String, t: List[String]): Call = {
      topicRoutes(s)(c,t.head)
    }
  }

  implicit class ConsumerRoute(s: String) {
    def consumerRouteMenuItem(cluster: String, consumer: String, consumerType: String): (String, Call) = {
      s -> consumerRoutes(s)(cluster,consumer,consumerType)
    }
    def consumerRoute(cluster: String, consumer: List[String], consumerType: String): Call = {
      consumerRoutes(s)(cluster,consumer.head, consumerType)
    }
  }

  implicit class LogkafkaRoute(s: String) {
    def logkafkaRouteMenuItem(c: String, h: String, l:String): (String, Call) = {
      s -> logkafkaRoutes(s)(c,h,l)
    }
    def logkafkaRoute(c: String, hl: List[String]): Call = {
      val logkafka_id = hl(0)
      val log_path = hl(1)
      logkafkaRoutes(s)(c,logkafka_id,log_path)
    }
  }
}
