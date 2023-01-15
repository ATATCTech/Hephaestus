package com.atatctech.packages.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log {
    public static class Time {
        public final static Time FUTURE = new Time(SpecialCase.FUTURE);
        public final static Time PAST = new Time(SpecialCase.PAST);
        protected final long baseTime;

        public Time() {
            baseTime = System.currentTimeMillis();
        }

        public Time(long baseTime) {
            if (baseTime < 0) throw new IllegalArgumentException("`baseTime` must be a positive integer.");
            this.baseTime = baseTime;
        }

        public Time(SpecialCase specialCase) {
            baseTime = (long) switch (specialCase) {
                case FUTURE -> Float.POSITIVE_INFINITY;
                case PAST -> Float.NEGATIVE_INFINITY;
            };
        }

        public static Time parseString(String baseTime, String format) {
            SimpleDateFormat form = new SimpleDateFormat(format);
            try {
                return new Time(form.parse(baseTime).getTime());
            } catch (ParseException e) {
                return new Time();
            }
        }

        public static Time parseString(String baseTime) {
            return parseString(baseTime, "yyyy-MM-dd HH:mm:ss");
        }

        protected static int getUnitCoefficients(Unit unit) {
            return switch (unit) {
                case Millisecond -> 1;
                case Second -> 1000;
                case Minute -> 60000;
                case Hour -> 3600000;
                case Day -> 86400000;
                case Week -> 604800000;
            };
        }

        // FixMe
//        protected static int getUnitCoefficients(Class<? extends TimeGap> unit) {
//            return switch (unit) {
//                case Milliseconds.class -> 1;
//                case Seconds.class -> 1000;
//                case Minutes.class -> 60000;
//                case Hours.class -> 3600000;
//                case Days.class -> 86400000;
//                case Weeks.class -> 604800000;
//            };
//        }

        public static long convert(Unit origin, Unit result, long value) {
            return value * getUnitCoefficients(origin) / getUnitCoefficients(result);
        }

        public static long calculateDuration(Runnable action, Unit unit) {
            long baseTime = System.currentTimeMillis();
            action.run();
            long endTime = System.currentTimeMillis();
            return (endTime - baseTime) / getUnitCoefficients(unit);
        }

        public static long calculateDuration(Time time1, Time time2, Unit unit) {
            return convert(Unit.Millisecond, unit, Math.abs(time1.getBaseTime() - time2.getBaseTime()));
        }

        public static boolean theSame(Time time1, Time time2, Unit unit) {
            return convert(Unit.Millisecond, unit, time1.getBaseTime()) == convert(Unit.Millisecond, unit, time2.getBaseTime());
        }

        public boolean isFuture() {
            return getBaseTime() > System.currentTimeMillis();
        }

        public boolean isCurrent() {
            return getBaseTime() == System.currentTimeMillis();
        }

        public boolean isPast() {
            return getBaseTime() < System.currentTimeMillis();
        }

        public Time forward(TimePeriod gap) {
            return new Time(getBaseTime() + gap.getMilliseconds());
        }

        public Time backward(TimePeriod gap) {
            return new Time(getBaseTime() - gap.getMilliseconds());
        }

        public String getStamp() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(new Date(getBaseTime()));
        }

        public String getStamp(String format) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date(getBaseTime()));
        }

        public long getDuration(Unit unit) {
            long duration = Math.abs(System.currentTimeMillis() - getBaseTime());
            return duration / getUnitCoefficients(unit);
        }

        public long getDuration() {
            return Math.abs(System.currentTimeMillis() - getBaseTime());
        }

        public Milliseconds getDurationAsGap() {
            return new Milliseconds(getDuration());
        }

        public long getDuration(Time time, Unit unit) {
            long duration = Math.abs(time.getBaseTime() - getBaseTime());
            return duration / getUnitCoefficients(unit);
        }

        public TimePeriod getDurationAsGap(Time time, Unit unit) {
            return TimePeriod.fromUnit(getDuration(time, unit), unit);
        }

        public long getBaseTime() {
            return baseTime;
        }

        public enum Unit {
            Week, Day, Hour, Minute, Second, Millisecond
        }

        public enum SpecialCase {
            FUTURE, PAST
        }

        public static class TimePeriod {
            public static TimePeriod fromUnit(long n, Unit unit) {
                return switch (unit) {
                    case Week -> new Weeks(n);
                    case Day -> new Days(n);
                    case Hour -> new Hours(n);
                    case Minute -> new Minutes(n);
                    case Second -> new Seconds(n);
                    case Millisecond -> new Milliseconds(n);
                };
            }

            protected final long milliseconds;

            public TimePeriod(long milliseconds) {
                this.milliseconds = milliseconds;
            }

            public long getMilliseconds() {
                return milliseconds;
            }
        }

        public static class Milliseconds extends TimePeriod {
            public Milliseconds(long milliseconds) {
                super(milliseconds);
            }
        }

        public static class Seconds extends Milliseconds {
            public Seconds(long seconds) {
                super(seconds * 1000);
            }
        }

        public static class Minutes extends Seconds {
            public Minutes(long minutes) {
                super(minutes * 60);
            }
        }

        public static class Hours extends Minutes {
            public Hours(long hours) {
                super(hours * 60);
            }
        }

        public static class Days extends Hours {
            public Days(long days) {
                super(days * 24);
            }
        }

        public static class Weeks extends Days {
            public Weeks(long weeks) {
                super(weeks * 7);
            }
        }
    }
}
