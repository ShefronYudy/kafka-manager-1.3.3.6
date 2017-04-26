/**
 * Copyright 2015 Yahoo Inc. Licensed under the Apache License, Version 2.0
 * See accompanying LICENSE file.
 */

package models.navigation

import play.api.mvc.Call

/**
 * @author hiral
 */
object BreadCrumbs {

  sealed trait BreadCrumb
  final case class BCDynamicText(cn: String => String) extends BreadCrumb
  final case class BCDynamicNamedLink(cn: String => String, cl : String => Call) extends BreadCrumb
  final case class BCDynamicMultiNamedLink(cn: String => String, cl : (String, List[String]) => Call) extends BreadCrumb
  final case class BCDynamicMultiNamedLink2(cn: String => String, cl : (String, List[String], String) => Call) extends BreadCrumb
  final case class BCDynamicLink(s: String, cl : String => Call) extends BreadCrumb
  final case class BCStaticLink(s: String, c: Call) extends BreadCrumb

  sealed trait BreadCrumbRendered
  final case class BCLink(s: String, url: String) extends BreadCrumbRendered
  final case class BCText(s: String) extends BreadCrumbRendered
  final case class BCActive(s: String) extends BreadCrumbRendered

  import models.navigation.QuickRoutes._

  val baseBreadCrumbs: Map[String, IndexedSeq[BreadCrumb]] = Map(
    "集群列表" -> IndexedSeq.empty[BreadCrumb],
    "添加集群" -> IndexedSeq("集群列表".baseRouteBreadCrumb)
  )

  val clusterBreadCrumbs: Map[String, IndexedSeq[BreadCrumb]] = Map(
    "未知集群操作" -> IndexedSeq("集群列表".baseRouteBreadCrumb),
    "删除集群" -> IndexedSeq("集群列表".baseRouteBreadCrumb, BCDynamicText(identity)),
    "禁用集群" -> IndexedSeq("集群列表".baseRouteBreadCrumb, BCDynamicText(identity)),
    "启用集群" -> IndexedSeq("集群列表".baseRouteBreadCrumb, BCDynamicText(identity)),
    "更新集群" -> IndexedSeq("集群列表".baseRouteBreadCrumb, BCDynamicText(identity)),
    "概要" -> IndexedSeq("集群列表".baseRouteBreadCrumb,BCDynamicText(identity)),
    "Broker列表" -> IndexedSeq("集群列表".baseRouteBreadCrumb,BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "Broker视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Broker列表".clusterRouteBreadCrumb),
    "Topic列表" -> IndexedSeq("集群列表".baseRouteBreadCrumb,BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "消费者列表" -> IndexedSeq("集群列表".baseRouteBreadCrumb,BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "创建Topic" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Topic列表".clusterRouteBreadCrumb),
    "Topic视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Topic列表".clusterRouteBreadCrumb),
    "消费者视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "消费者列表".clusterRouteBreadCrumb),
    "已消费Topic视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "消费者列表".clusterRouteBreadCrumb),
    "Logkafka列表" -> IndexedSeq("集群列表".baseRouteBreadCrumb,BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "创建Logkafka" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Logkafka列表".clusterRouteBreadCrumb),
    "Logkafka视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Logkafka列表".clusterRouteBreadCrumb),
    "首选副本选举" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "重新分配分区" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute)),
    "运行选举" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "首选副本选举".clusterRouteBreadCrumb
    )
  )

  val topicBreadCrumbs: Map[String, IndexedSeq[BreadCrumb]] = Map(
    "Topic视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Topic列表".clusterRouteBreadCrumb,
      BCDynamicMultiNamedLink(identity,"Topic视图".topicRoute)
    )
  )

  val consumerBreadCrumbs: Map[String, IndexedSeq[BreadCrumb]] = Map(
    "消费者视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "消费者列表".clusterRouteBreadCrumb,
      BCDynamicMultiNamedLink2(identity,"消费者视图".consumerRoute)
    )
  )
  val logkafkaBreadCrumbs: Map[String, IndexedSeq[BreadCrumb]] = Map(
    "Logkafka视图" -> IndexedSeq(
      "集群列表".baseRouteBreadCrumb,
      BCDynamicNamedLink(identity,"概要".clusterRoute),
      "Logkafka列表".clusterRouteBreadCrumb,
      BCDynamicMultiNamedLink(identity,"Logkafka视图".logkafkaRoute)
    )
  )

  def withView(s: String) : IndexedSeq[BreadCrumbRendered] = {
    val rendered : IndexedSeq[BreadCrumbRendered] = baseBreadCrumbs.getOrElse(s,IndexedSeq.empty[BreadCrumb]) map {
      case BCStaticLink(n,c) => BCLink(n,c.toString())
      case a: Any => throw new IllegalArgumentException(s"Only static link supported : $a")
    }
    rendered :+ BCActive(s)
  }

  private[this] def renderWithCluster(s: String, clusterName: String) : IndexedSeq[BreadCrumbRendered] = {
    clusterBreadCrumbs.getOrElse(s,IndexedSeq.empty[BreadCrumb]) map {
      case BCStaticLink(n,c) => BCLink(n,c.toString())
      case BCDynamicNamedLink(cn, cl) => BCLink(cn(clusterName),cl(clusterName).toString())
      case BCDynamicLink(cn, cl) => BCLink(cn,cl(clusterName).toString())
      case BCDynamicText(cn) => BCText(cn(clusterName))
      case _ => BCText("ERROR")
    }
  }

  def withNamedViewAndCluster(s: String, clusterName: String, name: String) : IndexedSeq[BreadCrumbRendered] = {
    renderWithCluster(s, clusterName) :+ BCActive(name)
  }

  def withViewAndCluster(s: String, clusterName: String) : IndexedSeq[BreadCrumbRendered] = {
    withNamedViewAndCluster(s, clusterName, s)
  }

  private[this] def renderWithClusterAndTopic(s: String, clusterName: String, topic: String) : IndexedSeq[BreadCrumbRendered] = {
    topicBreadCrumbs.getOrElse(s,IndexedSeq.empty[BreadCrumb]) map {
      case BCStaticLink(n,c) => BCLink(n,c.toString())
      case BCDynamicNamedLink(cn, cl) => BCLink(cn(clusterName),cl(clusterName).toString())
      case BCDynamicMultiNamedLink(cn, cl) => BCLink(cn(topic),cl(clusterName,List(topic)).toString())
      case BCDynamicLink(cn, cl) => BCLink(cn,cl(clusterName).toString())
      case BCDynamicText(cn) => BCText(cn(clusterName))
      case any => throw new UnsupportedOperationException(s"Unhandled breadcrumb : $any")
    }
  }

  private[this] def renderWithClusterAndConsumer(s: String, clusterName: String, consumer: String, consumerType: String, topic: String = "") : IndexedSeq[BreadCrumbRendered] = {
    consumerBreadCrumbs.getOrElse(s,IndexedSeq.empty[BreadCrumb]) map {
      case BCStaticLink(n,c) => BCLink(n,c.toString())
      case BCDynamicNamedLink(cn, cl) => BCLink(cn(clusterName),cl(clusterName).toString())
      case BCDynamicMultiNamedLink(cn, cl) => BCLink(cn(consumer),cl(clusterName,List(consumer)).toString())
      case BCDynamicLink(cn, cl) => BCLink(cn,cl(clusterName).toString())
      case BCDynamicText(cn) => BCText(cn(clusterName))
      case BCDynamicMultiNamedLink2(cn, cl) => BCLink(cn(consumer),cl(clusterName,List(consumer), consumerType).toString())
      case any => throw new UnsupportedOperationException(s"Unhandled breadcrumb : $any")
    }
  }

  def withNamedViewAndClusterAndTopic(s: String, clusterName: String, topic: String, name: String) : IndexedSeq[BreadCrumbRendered] = {
    renderWithClusterAndTopic(s, clusterName,topic) :+ BCActive(name)
  }

  private[this] def renderWithClusterAndLogkafka(s: String, clusterName: String, logkafka_id: String, log_path: String) : IndexedSeq[BreadCrumbRendered] = {
    val hl = logkafka_id + "?" + log_path
    logkafkaBreadCrumbs.getOrElse(s,IndexedSeq.empty[BreadCrumb]) map {
      case BCStaticLink(n,c) => BCLink(n,c.toString())
      case BCDynamicNamedLink(cn, cl) => BCLink(cn(clusterName),cl(clusterName).toString())
      case BCDynamicMultiNamedLink(cn, cl) => BCLink(cn(hl),cl(clusterName,List(logkafka_id, log_path)).toString())
      case BCDynamicLink(cn, cl) => BCLink(cn,cl(clusterName).toString())
      case BCDynamicText(cn) => BCText(cn(clusterName))
      case any => throw new UnsupportedOperationException(s"Unhandled breadcrumb : $any")
    }
  }

  def withNamedViewAndClusterAndLogkafka(s: String, clusterName: String, logkafka_id: String, log_path: String, name: String) : IndexedSeq[BreadCrumbRendered] = {
    renderWithClusterAndLogkafka(s, clusterName, logkafka_id, log_path) :+ BCActive(name)
  }
  def withNamedViewAndClusterAndConsumerWithType(s: String, clusterName: String, consumer: String, consumerType: String, name: String) : IndexedSeq[BreadCrumbRendered] = {
    renderWithClusterAndConsumer(s, clusterName, consumer, consumerType) :+ BCActive(name)
  }
}
