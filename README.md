# ElevatedPulseRTD 📊

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)]()

> **Real-time dashboard for monitoring system metrics, business KPIs, and custom events using Kafka Streams, InfluxDB, and WebSocket.**

![Dashboard Preview](docs/dashboard-preview.png)

## ✨ Features

- **🔴 Real-time Monitoring** - Live metrics streaming via WebSocket with < 1s latency
- **📊 Interactive Visualizations** - Dynamic charts with Chart.js (Bar, Line, Trend views)
- **⚡ Stream Processing** - Kafka Streams for real-time data aggregation and alerting
- **💾 Time-Series Storage** - InfluxDB for efficient metrics storage and querying
- **🎨 Modern UI** - Glassmorphism design with dark/light mode support
- **🔔 Smart Alerts** - Configurable thresholds with visual and toast notifications
- **📱 Responsive Design** - Works seamlessly on desktop, tablet, and mobile
- **📤 Data Export** - One-click CSV export and chart image download

## 🏗️ Architecture
┌─────────────────┐     ┌─────────────┐     ┌─────────────────┐
│   Web Client    │◄────┤  WebSocket  │◄────┤   Spring Boot   │
│  (Chart.js/STOMP)│     │   (/topic)  │     │    Backend      │
└─────────────────┘     └─────────────┘     └────────┬────────┘
│
┌────────────────────────┼────────────────────────┐
│                        │                        │
▼                        ▼                        ▼
┌─────────┐            ┌─────────────┐          ┌─────────────┐
│  Kafka  │───────────►│Kafka Streams│─────────►│  InfluxDB   │
│ Cluster │            │ (Aggregate) │          │ (Time-Series)│
└─────────┘            └─────────────┘          └─────────────┘
│
▼
┌─────────────┐
│   Alerts    │
│  (WebSocket)│
└─────────────┘

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for Kafka & InfluxDB)
- Node.js 18+ (optional, for frontend development)

### 1. Clone & Build

```
bash
git clone https://github.com/rchawes/ElevatedPulseRTD.git
cd ElevatedPulseRTD
mvn clean package -DskipTests

2. Start Infrastructure

# Using Docker Compose
docker-compose up -d kafka influxdb

# Or start individually
docker run -d --name kafka -p 9092:9092 confluentinc/cp-kafka:latest
docker run -d --name influxdb -p 8086:8086 influxdb:2.7

3. Configure Application
Create src/main/resources/application-local.properties:

Properties

# InfluxDB
influxdb.url=http://localhost:8086
influxdb.token=your-influxdb-token
influxdb.org=elevated-pulse
influxdb.bucket=metrics

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Server
server.port=8080

4. Run Application

# Development
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Production
java -jar target/ElevatedPulseRTD-*.jar

5. Access Dashboard
Open http://localhost:8080 in your browser.

Default Keyboard Shortcuts:
D - Toggle dark mode
S - Open settings
R - Refresh data
? - Show keyboard shortcuts

📖 API Documentation

REST Endpoints

Endpoint	            Method	      Description	        Request Body
/api/events/pageview	POST	      Record page view	    {"url": "/home", "timestamp": "..."}
/api/events/cpu	POST	Record        CPU metric	        {"host": "server-1", "usage": 45.2, "timestamp": "..."}

WebSocket Topics

Topic	        Direction	Description
/topic/stats	Subscribe	Real-time aggregated metrics
/topic/alerts	Subscribe	Critical system alerts
/app/setRange	Publish	    Update time range (minutes)

Example: Send CPU Metric

curl -X POST http://localhost:8080/api/events/cpu \
  -H "Content-Type: application/json" \
  -d '{
    "host": "web-server-01",
    "usage": 78.5,
    "timestamp": "2024-01-15T10:30:00Z"
  }'
  
⚙️ Configuration

Application Properties

Property	                     Default	                  Description
influxdb.url	                 http://localhost:8086	      InfluxDB connection URL
influxdb.bucket	                 metrics	                  Default bucket for metrics
spring.kafka.bootstrap-servers	 localhost:9092	              Kafka broker addresses
server.port	8080	             Application                  server port

Dashboard Settings

Access settings via the gear icon (⚙️) or press S:
Time Range: 1 min | 5 min | 10 min | 30 min | 1 hour
Refresh Rate: Auto (5s) or manual
CPU Alert Threshold: 50% - 95% (default: 80%)
Theme: Light / Dark / Auto

🧪 Testing

# Run unit tests
mvn test

# Run integration tests (requires Docker)
mvn verify -P integration-tests

# Generate coverage report
mvn jacoco:report

📁 Project Structure

ElevatedPulseRTD/
├── src/
│   ├── main/
│   │   ├── java/com/elevatedpulse/rtd/
│   │   │   ├── ElevatedPulseRtdApplication.java    # Entry point
│   │   │   ├── config/
│   │   │   │   ├── InfluxDBConfig.java             # Time-series DB config
│   │   │   │   ├── KafkaStreamsConfig.java         # Stream processing config
│   │   │   │   └── WebSocketConfig.java            # Real-time messaging config
│   │   │   ├── controller/
│   │   │   │   ├── EventController.java            # REST API endpoints
│   │   │   │   └── RangeController.java            # Time range settings
│   │   │   ├── service/
│   │   │   │   └── StatsService.java               # Metrics aggregation service
│   │   │   ├── consumer/
│   │   │   │   ├── AlertConsumer.java              # Alert processing
│   │   │   │   └── InfluxDBWriter.java             # Metrics persistence
│   │   │   └── model/
│   │   │       ├── Alert.java                      # Alert data model
│   │   │       ├── CpuMetricEvent.java             # CPU metric event
│   │   │       ├── PageViewEvent.java              # Page view event
│   │   │       └── MetricAggregate.java            # Aggregated metric
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html                      # Modern dashboard UI
│   │       └── application.properties              # App configuration
│   └── test/                                       # Unit & integration tests
├── docker-compose.yml                              # Infrastructure setup
├── pom.xml                                         # Maven dependencies
└── README.md                                       # This file

🤝 Contributing

Contributions are welcome! Please follow these steps:

Fork the repository
Create a feature branch (git checkout -b feature/amazing-feature)
Commit your changes (git commit -m 'Add amazing feature')
Push to the branch (git push origin feature/amazing-feature)
Open a Pull Request
Development Guidelines
Follow Google Java Style Guide
Write unit tests for new features
Update documentation for API changes
Ensure mvn clean verify passes before submitting

🔒 Security

Never commit application.properties with real credentials
Use environment variables for sensitive configuration:

export INFLUXDB_TOKEN=your-secret-token
Run with least-privilege database users
Enable HTTPS in production (see docs/security.md)

🐛 Troubleshooting

Connection Issues
Table
Symptom	Solution
WebSocket shows "Offline"	Verify Kafka and InfluxDB are running (docker ps)
No data in charts	Check if metrics are being sent to /api/events/*
Charts not updating	Press R to refresh or check browser console for errors
High CPU alert not firing	Adjust threshold in Settings (default: 80%)
Performance Tuning
For high-throughput scenarios (>10k events/sec):

Properties

# Increase Kafka parallelism
spring.kafka.listener.concurrency=10

# Enable batch processing
spring.kafka.producer.batch-size=65536
spring.kafka.producer.linger-ms=10

# InfluxDB write optimization
influxdb.write-buffer-size=10000

📚 Resources

Spring Boot Documentation
Kafka Streams Guide
InfluxDB 2.x API
Chart.js Documentation

📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

🙏 Acknowledgments

Spring Team for the excellent framework
Confluent for Kafka education resources
InfluxData for time-series database
Chart.js for beautiful visualizations

<p align="center">
  Made with ❤️ by <a href="https://github.com/rchawes">rchawes</a>
  <br>
  <a href="https://github.com/rchawes/ElevatedPulseRTD/issues">Report Bug</a> ·
  <a href="https://github.com/rchawes/ElevatedPulseRTD/issues">Request Feature</a>
</p>
```