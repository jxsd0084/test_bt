package com.github.trace.service;

import com.github.trace.entity.BuriedPoint;
import com.github.trace.mapper.BuriedPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送邮件
 * Created by hanmz on 2015/12/22.
 */
@Service
public class CEPService {

//  private long limit;
//  private String host;
//  private String username;
//  private String password;
//  private String address;
//  private List<String> recipient;

  @Autowired
  private BuriedPointMapper buriedPointMapper;

//  @Autowired
//  private InfluxOssService influxOssService;
//  @Autowired
//  private DailyMapper dailyMapper;

//  @PostConstruct
//  void init() {
//    ConfigFactory.getInstance().getConfig("oss-mail", this::loadConfig);
//  }
//
//  private void loadConfig(IConfig config) {
//    limit = config.getInt("limit", 1000);
//    host = config.get("host");
//    username = config.get("username");
//    password = config.get("password");
//    address = config.get("address");
//    recipient = Splitter.on(CharMatcher.anyOf(" ;,\t")).omitEmptyStrings().trimResults().splitToList(config.get("recipient"));
//  }

  /**
   * 存储日报
   */
//  public void storageDaily() throws Exception {
//    String day = DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY);
//    List<Map> recent = influxOssService.dailyService("1d", day);
//    if (recent != null && recent.size() > 0) {
//      Predicate<Map> predicate = i -> Long.parseLong((String) i.get("total")) > limit;
//      List<Map> filtered = recent.stream().filter(predicate).collect(Collectors.toList());
//      filtered.sort((a, b) -> {
//        double diff = Double.parseDouble((String)b.get("fail")) -  Double.parseDouble((String)a.get("fail"));
//        if (Math.abs(diff) < 0.000001) {
//          return 0;
//        } else if (diff > 0) {
//          return 1;
//        } else {
//          return -1;
//        }
//      });
//      if (filtered.size() > 100) {
//        filtered = filtered.subList(0, 100);
//      }
//      dailyMapper.insertDaily(day, InnerUtil.toJson(filtered));
//    }
//  }

  public List<BuriedPoint> getConfiguration() {
    return buriedPointMapper.findAll();
  }

  public List<BuriedPoint> getBuriedPointList(int parent_id, int child_id) {
    return buriedPointMapper.findByBizIds(parent_id, child_id);
  }

  public BuriedPoint getBuriedPoint(int id) {
    return buriedPointMapper.findById(id);
  }

//  /**
//   * 发送邮件
//   */
//  public void sendMail(String day, String receiver) throws Exception {
//    JavaMailSenderImpl sender = new JavaMailSenderImpl();
//    sender.setHost(host);
//    sender.setUsername(username);
//    sender.setPassword(password);
//
//    MimeMessage message = sender.createMimeMessage();
//    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
//
//    Date now = new Date();
//    helper.setFrom(address);
//    if (Strings.isNullOrEmpty(receiver)) {
//      helper.setTo(recipient.toArray(new String[recipient.size()]));
//    } else {
//      List<String> items = Splitter.on(CharMatcher.anyOf(";, \t")).splitToList(receiver);
//      helper.setTo(items.toArray(new String[items.size()]));
//    }
//    if (Strings.isNullOrEmpty(day)) {
//      day = DateUtil.formatYmd(System.currentTimeMillis() - InnerUtil.ONE_DAY);
//    }
//    helper.setSubject("蜂眼监控日报-" + day);
//    helper.setSentDate(now);
//    String html = createMailHtml(day);
//    if (Strings.isNullOrEmpty(html)) {
//      throw new RuntimeException("cannot generate mail body");
//    }
//    helper.setText(html, true);
//    sender.send(message);
//  }
//
//  private String createMailHtml(String day) {
//    JetTemplate template = JetWebEngine.getEngine().getTemplate("daily/mail.jetx");
//    StringWriter out = new StringWriter();
//    Map<String, Object> model = new HashMap<>();
//    String s = dailyMapper.findByDay(day);
//    if (Strings.isNullOrEmpty(s)) {
//      throw new IllegalStateException("读取数据库失败");
//    }
//    JSONArray obj = (JSONArray) (JSON.parse(s));
//    List<Oss> top = obj.stream().map(i -> {
//      JSONArray j = (JSONArray) i;
//      Oss oss = new Oss();
//      oss.setName(j.getString(0));
//      oss.setDesc(j.getString(1));
//      oss.setTotal(j.getLong(2));
//      oss.setFailNum(j.getLong(3));
//      oss.setFail(j.getDouble(4));
//      oss.setSlow(j.getDouble(5));
//      oss.setCost(j.getDouble(6));
//      return oss;
//    }).collect(toList());
//    model.put("day", day);
//    model.put("parameter", "&last=other&start="+day+" 00:00:00&end="+day+" 23:59:59");
//    model.put("data", top);
//    template.render(model, out);
//    return out.toString();
//  }

}
